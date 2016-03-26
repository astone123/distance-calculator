DistanceCalculator
======================
Version: 1.0
Language: Java

Author: Adam Stone [@theaveragestone](http://www.twitter.com/theaveragestone)

## What Does DistanceCalculator do?
DistanceCalculator is a program that allows you to calculate the distance in miles (rounded to nearest 100th of a mile) between addresses using Google's Distance Matrix API.

##Installation
Download a zip of the project and extract to your local directory.

##Usage
Run DistanceCalculator from command prompt or terminal. DistanceCalculator.jar takes three parameters.

1.) File name of input file. The input is a text file and should contain addresses or zip codes separated by line breaks.
2.) Address to compare to. This is the address or zip code that your input file addresses will be compared to.
3.) Google Distance Matrix API Key. You will need to create a project with Google Matrix API to get a key [here](https://developers.google.com/maps/documentation/distance-matrix/).

Execution should look like this:

```ruby
java -jar DistanceCalculator.jar input.txt 21345 API_KEY
```
Be sure to replace 'input.txt' with your input file name, '21345' with your desired address or zip code, and 'API_KEY' with your API Key.

When the program is finished running, the data will be written to an output text file named '*inputfilenameOutput.txt*' in the project folder.

Make sure to read through [Google Maps Distance Matrix API Guide](https://developers.google.com/maps/documentation/distance-matrix/intro#Introduction) for proper parameter formatting.
