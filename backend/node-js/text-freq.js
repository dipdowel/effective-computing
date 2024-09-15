const fs = require('fs');
const readline = require('readline');

async function countWords(inputFile, outputFile) {
    const wordRegex = /[A-Za-z0-9-]+/g;
    const wordCounts = {};
    wordCounts['constructor'] = 0; // work around the constructor bug

    const fileStream = fs.createReadStream(inputFile);
    const rl = readline.createInterface({
        input: fileStream,
        crlfDelay: Infinity
    });

    for await (const line of rl) {
        const words = line.toLowerCase().match(wordRegex) || [];
        for (const word of words) {
            if (wordCounts[word]) {
                wordCounts[word]++;
            } else {
                wordCounts[word] = 1;
            }
        }
    }

    const sortedWordCounts = Object.entries(wordCounts)
        .sort((a, b) => b[1] - a[1] || a[0].localeCompare(b[0]));

    let outputData = sortedWordCounts.map(([word, count]) => `${count} : ${word}`).join('\n');
    
    outputData += "\n";

    fs.writeFileSync(outputFile, outputData, 'utf-8');
}

if (process.argv.length !== 4) {
    console.log("Usage: node word_count.js <input_file> <output_file>");
    process.exit(1);
}

const inputFile = process.argv[2];
const outputFile = process.argv[3];

countWords(inputFile, outputFile);
