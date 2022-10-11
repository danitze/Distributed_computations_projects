package main

import (
	context2 "context"
	"fmt"
	"github.com/marusama/cyclicbarrier"
	"math/rand"
	"strconv"
	"strings"
	"time"
)

func elementsSum(array *[]int) int {
	sum := 0
	for _, value := range *array {
		sum = sum + value
	}
	return sum
}

func randBool() bool {
	rand.Seed(time.Now().UnixNano())
	return rand.Intn(2) == 1
}

func main() {

	arr1 := []int{4, 7, 3, 4}
	arr2 := []int{1, -1, -5, 2}
	arr3 := []int{1, 4, 2, 5}

	areSumsEqual := false

	mediumSum := (elementsSum(&arr1) + elementsSum(&arr2) + elementsSum(&arr3)) / 3

	barrier := cyclicbarrier.NewWithAction(3, func() error {
		arr1Sum := elementsSum(&arr1)
		areSumsEqual = arr1Sum == elementsSum(&arr2) && arr1Sum == elementsSum(&arr3)
		mediumSum = (elementsSum(&arr1) + elementsSum(&arr2) + elementsSum(&arr3)) / 3
		fmt.Println("--------")
		time.Sleep(100 * time.Millisecond)
		return nil
	})

	go compute(&arr1, 1, &barrier, &areSumsEqual, &mediumSum)
	go compute(&arr2, 2, &barrier, &areSumsEqual, &mediumSum)
	go compute(&arr3, 3, &barrier, &areSumsEqual, &mediumSum)

	time.Sleep(10 * time.Minute)
}

func compute(arr *[]int, num int, barrier *cyclicbarrier.CyclicBarrier, areSumsEqual *bool, mediumSum *int) {
	for !(*areSumsEqual) {
		if elementsSum(arr) < *mediumSum {
			(*arr)[0]++
		} else if elementsSum(arr) > *mediumSum {
			(*arr)[0]--
		}
		fmt.Println("Arr " + strconv.Itoa(num) + ": [" + strings.Trim(strings.Join(strings.Split(fmt.Sprint(*arr), " "), ", "), "[]") + "]")
		(*barrier).Await(context2.TODO())
		(*barrier).Reset()
	}
}
