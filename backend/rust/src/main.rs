use std::collections::HashMap;
use std::env;
use std::fs::File;
use std::io::{self, BufRead, BufReader, Write};

fn main() -> io::Result<()> {
    let args: Vec<String> = env::args().collect();
    if args.len() != 3 {
        eprintln!("Usage: word_count <input_file> <output_file>");
        std::process::exit(1);
    }

    let input_file = &args[1];
    let output_file = &args[2];

    let word_counts = count_words(input_file)?;

    // Sort the results and write them to the output file.
    write_output(output_file, &word_counts)?;

    Ok(())
}

/// Count the number of times each word appears in the file.
fn count_words(filename: &str) -> io::Result<HashMap<String, u32>> {
    let file = File::open(filename)?;
    let reader = BufReader::new(file);

    let mut word_counts: HashMap<String, u32> = HashMap::new();

    // We consider a word to be any sequence of letters, numbers, or hyphens.
    let word_pattern = regex::Regex::new(r"[A-Za-z0-9-]+").unwrap();

    for line in reader.lines() {
        let line = line?.to_lowercase();
        for word in word_pattern.find_iter(&line) {
            let word = word.as_str().to_string();
            *word_counts.entry(word).or_insert(0) += 1;
        }
    }

    Ok(word_counts)
}

fn write_output(filename: &str, word_counts: &HashMap<String, u32>) -> io::Result<()> {
    let mut word_count_vec: Vec<_> = word_counts.iter().collect();

    // First sort by count, then by word.
    word_count_vec.sort_by(|a, b| b.1.cmp(a.1).then_with(|| a.0.cmp(b.0)));

    let mut file = File::create(filename)?;
    for (word, count) in word_count_vec {
        writeln!(file, "{} : {}", count, word)?;
    }

    Ok(())
}
