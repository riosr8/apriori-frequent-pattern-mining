
# Frequent pattern mining using the Apriori algorithm

This project was done in OS X environment using Java v1.8 SDK.

## Input

The input files can be found in the `input` directory.

Each row represents a transaction that contains product ids.

## Instructions

To run from command line:

1. Go to directory where source files are located in (src).
2. `javac *.java`

3. If arguments are provided via command line, they should be in the specified format below:

`java Apriori minsup_value k_value input_transaction_file_path output_file_path`

Otherwise, you can alternatively simply use:

`java Apriori`

You will then be prompted for the the minimum support, k value, and both input and output file paths.



*If running on Windows be sure to have set the path to jdk so the java command is recognized.
Example to set in command prompt:
	`set "path=%path%;c:\program files\java\jdk1.8.0_144\bin"`
