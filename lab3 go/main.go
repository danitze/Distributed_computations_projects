package main

import (
	"fmt"
	"math/rand"
	"strconv"
	"time"
)

var completionChannel = make(chan int)
var smokerChannels = [3]chan int{make(chan int), make(chan int), make(chan int)}
var items = [3]string{"tobacco", "paper", "matches"}

func main() {
	go mediator()
	for i := 0; i < 3; i++ {
		go smoker("Smoker "+strconv.Itoa(i+1), i)
	}
	time.Sleep(10 * time.Minute)
}

type TableItems struct {
	item1 int
	item2 int
}

func generateItems() TableItems {
	rand.Seed(time.Now().UnixNano())
	var item1 = rand.Intn(3)
	var item2 = item1
	for item2 == item1 {
		item2 = rand.Intn(3)
	}
	return TableItems{item1: item1, item2: item2}
}

func getAbsentItemNumber(tableItems TableItems) int {
	for i := 0; i < 3; i++ {
		if tableItems.item1 != i && tableItems.item2 != i {
			return i
		}
	}
	return -1
}

func mediator() {
	for {
		tableItems := generateItems()
		println("Items on table: " + items[tableItems.item1] + ", " + items[tableItems.item2])
		smokerNumber := getAbsentItemNumber(tableItems)
		smokerChannels[smokerNumber] <- 0
		<-completionChannel
	}
}

func smoker(name string, itemNumber int) {
	for {
		<-smokerChannels[itemNumber]
		fmt.Println("Smoker " + name + " with item " + items[itemNumber] + " smokes a cigarette")
		time.Sleep(2 * time.Second)
		completionChannel <- 0
	}
}
