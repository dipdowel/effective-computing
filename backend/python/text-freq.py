import sys
import re
from collections import Counter

def count_words(input_file, output_file):
    # Regular expression to match words as defined (letters, numbers, hyphens)
    word_regex = re.compile(r'[A-Za-z0-9-]+')
    
    # Initialize an empty Counter to store word frequencies
    word_counts = Counter()

    # Open the input file and read it line by line
    with open(input_file, 'r', encoding='utf-8') as file:
        for line in file:
            # Convert the line to lowercase and find all words
            words = word_regex.findall(line.lower())
            # Update the Counter with words found in this line
            word_counts.update(words)

    # Sort by frequency (descending) and then alphabetically by word
    sorted_word_counts = sorted(word_counts.items(), key=lambda item: (-item[1], item[0]))
    
    # Write the result to the output file
    with open(output_file, 'w', encoding='utf-8') as file:
        for word, count in sorted_word_counts:
            file.write(f'{count} : {word}\n')

if __name__ == '__main__':
    if len(sys.argv) != 3:
        print("Usage: python3 word_count.py <input_file> <output_file>")
        sys.exit(1)
    
    input_file = sys.argv[1]
    output_file = sys.argv[2]
    
    count_words(input_file, output_file)
