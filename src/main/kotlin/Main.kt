package de.porsche.sloffer

import java.util.LinkedList
import java.util.Queue
import java.util.Stack


data class Node(val value: Int, var left: Node?, var right: Node?)

fun main() {
    val list = ArrayList(listOf(5, 2, 4, 5, 6, 7, 2, 3, 5, 111, 55, 33))
    val sorted = sort(list)
    println(sorted)
}

fun sort(list: ArrayList<Int>): Stack<Int> {
    heapify(list)
    val res = Stack<Int>()
    while (list.isNotEmpty()) {
        val max = pop(list)
        res.push(max)
    }

    return res
}

fun heapify(heap: ArrayList<Int>) {
    for (index in heap.size - 1 downTo 0) {
        val parentIndex = getParentIndex(index)


        if (heap[index] > heap[parentIndex]) {
            swap(heap, index, parentIndex)

            bubbleDown(heap, index)
        }
    }
}

fun pop(heap: ArrayList<Int>): Int {
    val first = heap[0]
    heap[0] = heap[heap.size - 1]
    heap.removeAt(heap.size - 1)

    bubbleDown(heap, 0)

    return first
}

fun bubbleDown(heap: ArrayList<Int>, startIndex: Int) {
    val swappedItems: Queue<Int> = LinkedList<Int>()
    swappedItems.add(startIndex)

    while (swappedItems.peek() != null) {
        val currentIndex = swappedItems.remove()
        val (leftChildIndex, rightChildIndex) = getChildIndices(currentIndex)

        if (leftChildIndex < heap.size && heap[leftChildIndex] > heap[currentIndex]) {
            swap(heap, leftChildIndex, currentIndex)
            swappedItems.add(leftChildIndex)
        }

        if (rightChildIndex < heap.size && heap[rightChildIndex] > heap[currentIndex]) {
            swap(heap, rightChildIndex, currentIndex)
            swappedItems.add(rightChildIndex)
        }
    }


}

fun swap(heap: ArrayList<Int>, child: Int, parent: Int) {
    val oldChildVal = heap[child]
    heap[child] = heap[parent]
    heap[parent] = oldChildVal


}

fun getChildIndices(index: Int): Pair<Int, Int> {
    val leftChildIndex = index * 2 + 1
    val rightChildIndex = index * 2 + 2

    return Pair(leftChildIndex, rightChildIndex)
}

fun getParentIndex(index: Int) = (index - 1) / 2


fun printGraphWithQueue(rootNode: Node) {
    val queue: Queue<Node> = LinkedList<Node>()
    queue.add(rootNode)

    while (queue.peek() != null) {
        val nextLineItems = LinkedList<Node>()
        do {
            val current = queue.remove()
            print("${current.value} ")
            if (current.left != null) nextLineItems.add(current.left!!)
            if (current.right != null) nextLineItems.add(current.right!!)

        } while (queue.peek() != null)

        queue.addAll(nextLineItems)
        println()
    }
}


fun printGraphWithIR(rootNode: Node) {
    // create intermediate representation
    val mapping = printableMapping(rootNode, 0, mutableMapOf())

    for ((_, nodes) in mapping.toSortedMap()) {
        val str = nodes.fold("") { string, node -> string + " ${node.value}" }
        println(str)
    }
}

fun printableMapping(
    node: Node?,
    line: Int,
    mapping: MutableMap<Int, MutableList<Node>>
): MutableMap<Int, MutableList<Node>> {
    if (node == null) return mapping

    val nextLine = line + 1
    mapping.merge(line, mutableListOf(node)) { current, _ ->
        current.add(node)
        current
    }

    printableMapping(node.left, nextLine, mapping)
    printableMapping(node.right, nextLine, mapping)

    return mapping
}


fun create(inputList: Array<Int>): Node {
    var rootNode = Node(inputList[0], null, null)
    var nextParent: Queue<Node> = LinkedList<Node>(listOf(rootNode))

    for (item in inputList.drop(1)) {
        val currentNode = nextParent.peek()
        val newNode = Node(item, null, null)
        nextParent.add(newNode)

        if (currentNode.left == null) {
            currentNode.left = newNode
        } else {
            currentNode.right = newNode
            nextParent.remove()
        }
    }

    return rootNode
}
