package main

import (
	"fmt"
	"math/rand"
	"strconv"
	"sync"
	"time"
)

type Port struct {
	capacity          int
	currentContainers int
	mutex             *sync.Mutex
	condition         *sync.Cond
}

func main() {
	lock := sync.Mutex{}
	condition := sync.NewCond(&lock)
	port := Port{capacity: 50, currentContainers: 0, mutex: &lock, condition: condition}
	for i := 0; i < 5; i++ {
		go dock(i+1, &port)
	}
	time.Sleep(10 * time.Minute)
}

func addContainers(port *Port, amount int, dockNumber int) {
	port.mutex.Lock()
	if amount > port.capacity {
		return
	}
	for port.currentContainers+amount > port.capacity {
		port.condition.Wait()
	}
	port.currentContainers += amount
	fmt.Println("Added " + strconv.Itoa(amount) + " to port at dock " + strconv.Itoa(dockNumber) + ". Now amount is " + strconv.Itoa(port.currentContainers))
	port.condition.Broadcast()
	port.mutex.Unlock()
}

func removeContainers(port *Port, amount int, dockNumber int) {
	port.mutex.Lock()
	if amount > port.capacity {
		return
	}
	for port.currentContainers < amount {
		port.condition.Wait()
	}
	port.currentContainers -= amount
	fmt.Println("Removed " + strconv.Itoa(amount) + " from port at dock " + strconv.Itoa(dockNumber) + ". Now amount is " + strconv.Itoa(port.currentContainers))
	port.condition.Broadcast()
	port.mutex.Unlock()
}

func dock(dockNumber int, port *Port) {
	for {
		shouldReload := rand.Intn(2)
		shipCapacity := rand.Intn(port.capacity-1) + 1
		isEmpty := rand.Intn(2)
		if isEmpty == 0 {
			if shipCapacity == 1 {
				addContainers(port, 1, dockNumber)
			} else {
				addContainers(port, rand.Intn(shipCapacity-1)+1, dockNumber)
			}
		}
		if shouldReload == 1 {
			removeContainers(port, shipCapacity, dockNumber)
		}
		time.Sleep(1 * time.Second)
	}
}
