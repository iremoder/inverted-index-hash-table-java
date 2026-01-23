## Project Overview
The objective of this project is to design and implement a search engine capable of indexing and retrieving documents from the BBC dataset. The core of the system is a custom hash table data structure. The project investigates the performance of different hashing strategies by comparing two hash functions (Simple Summation Function and Polynomial Accumulation Function) and two collision resolution techniques (Linear Probing and Double Hashing) under varying load factors.


## Core Data Structures
- **HashTable**  
  Custom generic hash table implementation using open addressing.
- **HashEntry**  
  Stores a word (key) and its corresponding posting list.
- **PostingList**  
  Keeps a list of documents where a word appears.
- **DocumentEntry**  
  Stores a document name and the frequency of the word in that document.


## Interfaces

- **IHashTable<K, V>**  
  Defines core operations: `put`, `get`, `remove`, and `resize`.
- **IHashFunction**  
  Provides a pluggable hash function mechanism.
- **ICollisionResolver**  
  Defines probing behavior for collision resolution.

This design allows easy comparison of different hashing strategies.

## Hash Functions
- **Simple Summation Function (SSF)**  
  Computes the hash by summing ASCII values of characters.
- **Polynomial Accumulation Function (PAF)**  
  Uses Horner’s Rule with a constant multiplier (z = 33) for better distribution.

## Collision Resolution Strategies
- **Linear Probing (LP)**  
  Resolves collisions by checking the next available slot sequentially.
- **Double Hashing (DH)**  
  Uses a second hash function to determine probe step size, reducing clustering.

## File Processing Pipeline

1. Load stop words from stop_words_en.txt
2. Traverse BBC dataset folders
3. Tokenize text using predefined delimiters
4. Ignore stop words and short tokens
5. Insert words into the hash table with document tracking
   
## Resources
In this project, all input files required for building the inverted index were placed inside a dedicated folder named resources within the project directory. This folder includes the following items:

1. **stop_words_en.txt:** list of English stop-words
2. **delimiters.txt:** characters used for splitting text
3. **bbc:** the BBC news dataset consisting of multiple categories and .txt documents
4. **1000.txt:** the file used for measuring search performance

## Output Description

The output shows the performance of the inverted index built using a custom hash table.
First, the program prints the total indexing time and the number of collisions that occurred while inserting words into the hash table.

When a word is searched, the output lists the documents that contain the word and how many times it appears in each document.

Finally, the program measures search performance by printing the minimum, maximum, and average search times. These results help evaluate how efficient the hash table implementation is.

## Performance Matrix

The following matrix summarizes the performance of the system under different configurations. The metrics include indexing time (time to process the dataset), total collisions (total probing steps during indexing), and search time (min, max, and average time to search 1000 words).

<img width="915" height="522" alt="Ekran görüntüsü 2026-01-21 173439" src="https://github.com/user-attachments/assets/9fe48cc9-5463-47aa-b275-e3af9f273beb" />

## Performance Analysis and Discussion
The performance matrix provides clear evidence regarding the efficiency of different hashing strategies.
- **The Impact of Primary Clustering (SSF + LP):**
The combination of the Simple Summation Function (SSF) and Linear Probing (LP) resulted in the worst performance, generating over 2 billion collisions at a 50% load factor. This number confirms that SSF generates poor key distribution, and LP exacerbates the issue through primary clustering, where long chains of occupied slots form. This led to the slowest indexing time (14 seconds).
- **Effectiveness of Double Hashing (DH):**
Using Double Hashing with SSF significantly improved performance, reducing collisions from ~2 billion to approximately 495,000. This demonstrates DH's ability to mitigate clustering by using a secondary hash function to calculate variable jump sizes, effectively spreading records even when the primary hash function is weak.
- **Superiority of Polynomial Hash (PAF):**
The Polynomial Accumulation Function (PAF) consistently outperformed SSF. By using Horner’s Rule, PAF distributed keys more uniformly across the table, resulting in the lowest collision counts (approximately 114,000 at α=0.5) and the fastest indexing times (~1 second).
- **Load Factor and Robustness:**
As the Load Factor increased from 0.5 to 0.8, collision counts naturally increased due to reduced table availability. However, Double Hashing proved to be more robust than Linear Probing at high density. For instance, with PAF at 80% load, DH maintained fewer collisions (341k) compared to LP (388k), validating DH as the superior collision resolution strategy for denser tables.
- **Search Time:**
Average search times directly correlated with collision counts. The SSF+LP configuration required significantly more time to retrieve keys (Avg: 0.0852 ms) compared to the PAF configurations (Avg: ~0.0005 ms), as the search algorithm was forced to traverse long probe sequences to locate entries.





