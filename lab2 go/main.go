package main

import "fmt"

//task b

var wareHouse = [10]int{100, 40, 3, 50, 43, 78, 22, 11, 67, 18}

var toTruckItemsChannel = make(chan int)
var toCountItemsChannel = make(chan int)

func main() {
	go moveItemsToTruck()
	go moveItemsToCount()
	fmt.Println(countItems())
}

func moveItemsToTruck() {
	for _, price := range wareHouse {
		toTruckItemsChannel <- price
	}
	close(toTruckItemsChannel)
}

func moveItemsToCount() {
	for {
		price, isOpened := <-toTruckItemsChannel
		if !isOpened {
			break
		}
		toCountItemsChannel <- price
	}
	close(toCountItemsChannel)
}

func countItems() int {
	count := 0
	for {
		price, isOpened := <-toCountItemsChannel
		if !isOpened {
			break
		}
		count += price
	}
	return count
}
