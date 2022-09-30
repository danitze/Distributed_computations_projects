package main

import (
	"fmt"
	"strconv"
	"sync"
	"time"
)

func main() {
	var mutex sync.RWMutex
	var graph [][]int
	deletedMap := make(map[int]bool)
	graph = addCity(&mutex, &graph)
	graph = addCity(&mutex, &graph)
	graph = addCity(&mutex, &graph)
	go addRoute(&mutex, &graph, &deletedMap, 0, 1, 20)
	go addRoute(&mutex, &graph, &deletedMap, 1, 2, 30)
	go searchTicketPrice(&mutex, 0, 1, &graph, &deletedMap)
	go searchTicketPrice(&mutex, 0, 2, &graph, &deletedMap)
	go deleteCity(&mutex, &deletedMap, &graph, 1)
	go searchTicketPrice(&mutex, 0, 1, &graph, &deletedMap)
	go searchTicketPrice(&mutex, 0, 2, &graph, &deletedMap)
	time.Sleep(60 * time.Minute)
}

func changeTicketPrice(mutex *sync.RWMutex, graph *[][]int, deletedMap *map[int]bool, firstCity int, secondCity int, newPrice int) {
	mutex.Lock()
	fmt.Println("Lock change ticket price")
	if firstCity >= len(*graph) || secondCity >= len(*graph) || firstCity < 0 || secondCity < 0 {
		fmt.Println("Wrong city numbers")
		fmt.Println("Unlock change ticket price")
		mutex.Unlock()
		return
	}
	if (*graph)[firstCity][secondCity] == 0 {
		fmt.Println("No route found")
		fmt.Println("Unlock change ticket price")
		mutex.Unlock()
		return
	}
	if (*deletedMap)[firstCity] || (*deletedMap)[secondCity] {
		fmt.Println("One of the cities is deleted")
		fmt.Println("Unlock change ticket price")
		mutex.Unlock()
		return
	}
	if newPrice < 1 {
		fmt.Println("Invalid price")
		fmt.Println("Unlock change ticket price")
		mutex.Unlock()
		return
	}
	(*graph)[firstCity][secondCity] = newPrice
	(*graph)[secondCity][firstCity] = newPrice
	fmt.Println("Unlock change ticket price")
	mutex.Unlock()
}

func addRoute(mutex *sync.RWMutex, graph *[][]int, deletedMap *map[int]bool, firstCity int, secondCity int, price int) {
	mutex.Lock()
	fmt.Println("Lock add route")
	if firstCity >= len(*graph) || secondCity >= len(*graph) || firstCity < 0 || secondCity < 0 {
		fmt.Println("Wrong city numbers")
		fmt.Println("Unlock add route")
		mutex.Unlock()
		return
	}
	if (*graph)[firstCity][secondCity] != 0 {
		fmt.Println("Route already exists")
		fmt.Println("Unlock add route")
		mutex.Unlock()
		return
	}
	if (*deletedMap)[firstCity] || (*deletedMap)[secondCity] {
		fmt.Println("One of the cities is deleted")
		fmt.Println("Unlock add route")
		mutex.Unlock()
		return
	}
	if price < 1 {
		fmt.Println("Invalid price")
		fmt.Println("Unlock add route")
		mutex.Unlock()
		return
	}
	(*graph)[firstCity][secondCity] = price
	(*graph)[secondCity][firstCity] = price
	fmt.Println("Unlock add route")
	mutex.Unlock()
}

func deleteRoute(mutex *sync.RWMutex, graph *[][]int, deletedMap *map[int]bool, firstCity int, secondCity int) {
	mutex.Lock()
	fmt.Println("Lock delete route")
	if firstCity >= len(*graph) || secondCity >= len(*graph) || firstCity < 0 || secondCity < 0 {
		fmt.Println("Wrong city numbers")
		fmt.Println("Unlock delete route")
		mutex.Unlock()
		return
	}
	if (*graph)[firstCity][secondCity] == 0 {
		fmt.Println("No route found")
		fmt.Println("Unlock delete route")
		mutex.Unlock()
		return
	}
	if (*deletedMap)[firstCity] || (*deletedMap)[secondCity] {
		fmt.Println("One of the cities is deleted")
		fmt.Println("Unlock delete route")
		mutex.Unlock()
		return
	}
	(*graph)[firstCity][secondCity] = 0
	(*graph)[secondCity][firstCity] = 0
	fmt.Println("Unlock delete route")
	mutex.Unlock()
}

func addCity(mutex *sync.RWMutex, graph *[][]int) [][]int {
	mutex.Lock()
	fmt.Println("Lock add city")
	resultGraph := append(*graph, []int{})
	for i := 0; i < len(resultGraph)-1; i++ {
		resultGraph[i] = append(resultGraph[i], 0)
	}
	arr := make([]int, len(resultGraph))
	resultGraph[len(resultGraph)-1] = append(resultGraph[len(resultGraph)-1], arr...)
	fmt.Println("Unlock add city")
	mutex.Unlock()
	return resultGraph
}

func deleteCity(mutex *sync.RWMutex, deletedMap *map[int]bool, graph *[][]int, cityNum int) {
	mutex.Lock()
	fmt.Println("Lock delete city")
	if cityNum < 0 || cityNum >= len(*graph) {
		fmt.Println("Wrong city number")
		fmt.Println("Unlock delete city")
		mutex.Unlock()
		return
	}
	(*deletedMap)[cityNum] = true
	for i := 0; i < len((*graph)[cityNum]); i++ {
		if (*graph)[cityNum][i] != 0 {
			(*graph)[cityNum][i] = 0
			(*graph)[i][cityNum] = 0
		}
	}
	fmt.Println("Unlock delete city")
	mutex.Unlock()
}

func searchTicketPrice(mutex *sync.RWMutex, cityA int, cityB int, graph *[][]int, deletedMap *map[int]bool) {
	mutex.RLock()
	fmt.Println("Lock search ticket price")
	if (*graph)[cityA][cityB] != 0 {
		fmt.Println("Direct way with price " + strconv.Itoa((*graph)[cityA][cityB]))
		fmt.Println("Unlock search ticket price")
		mutex.RUnlock()
		return
	}
	visited := make([]bool, len(*graph))
	price := dfs(graph, deletedMap, &visited, cityA, cityB, 0)
	if price > 0 {
		fmt.Println("Price " + strconv.Itoa(price))
	} else {
		fmt.Println("No way exists")
	}
	fmt.Println("Unlock search ticket price")
	mutex.RUnlock()
}

func dfs(graph *[][]int, deletedMap *map[int]bool, visited *[]bool, current int, target int, currentPrice int) int {
	(*visited)[current] = true
	if current == target {
		return currentPrice
	}
	for i := 0; i < len((*graph)[current]); i++ {
		if (*deletedMap)[i] {
			continue
		}
		if (*graph)[current][i] > 0 && !(*visited)[i] {
			price := dfs(graph, deletedMap, visited, i, target, currentPrice+(*graph)[current][i])
			if price > 0 {
				return price
			}
		}
	}
	return 0
}
