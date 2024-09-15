package main

import (
    "bufio"
    "fmt"
    "os"
    "regexp"
    "sort"
    "strings"
)

func main() {
    if len(os.Args) != 3 {
        fmt.Println("Usage: go run word_count.go <input_file> <output_file>")
        os.Exit(1)
    }
    inputFile := os.Args[1]
    outputFile := os.Args[2]

    if err := countWords(inputFile, outputFile); err != nil {
        fmt.Println("Error:", err)
        os.Exit(1)
    }
}

func countWords(inputFile, outputFile string) error {
    file, err := os.Open(inputFile)
    if err != nil {
        return err
    }
    defer file.Close()

    scanner := bufio.NewScanner(file)
    wordRegex := regexp.MustCompile(`[A-Za-z0-9-]+`)
    wordCounts := make(map[string]int)
    wordCounts["constructor"] = 0 // Replicating the JavaScript workaround

    for scanner.Scan() {
        line := strings.ToLower(scanner.Text())
        words := wordRegex.FindAllString(line, -1)
        for _, word := range words {
            wordCounts[word]++
        }
    }

    if err := scanner.Err(); err != nil {
        return err
    }

    type wordCount struct {
        word  string
        count int
    }
    var counts []wordCount
    for word, count := range wordCounts {
        counts = append(counts, wordCount{word, count})
    }

    sort.Slice(counts, func(i, j int) bool {
        if counts[i].count != counts[j].count {
            return counts[i].count > counts[j].count // Descending count
        }
        return counts[i].word < counts[j].word // Ascending word
    })

    outputFileHandle, err := os.Create(outputFile)
    if err != nil {
        return err
    }
    defer outputFileHandle.Close()

    writer := bufio.NewWriter(outputFileHandle)
    for _, wc := range counts {
        if _, err := fmt.Fprintf(writer, "%d : %s\n", wc.count, wc.word); err != nil {
            return err
        }
    }

    // Ensure the final newline is present
    if _, err := writer.WriteString("\n"); err != nil {
        return err
    }
    return writer.Flush()
}
