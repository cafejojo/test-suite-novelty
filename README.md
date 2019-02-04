# test-suite-novelty

Tool for determining the novelty of a test suite with respect to a baseline test suite. It compares two JUnit test suites (a "new" one and a baseline) test case by test case. For each, it analyzes the set of items covered by each test in the new test suite against each baseline test. If none of the sets of items covered is a subset of a certain new test case, that test case is considered "new". The output of this tool gives the number of new test cases scaled by the total LOC count of the code base under test.

## Requirements and Installation
Schaapi requires JRE 8 and has been tested on Windows and Unix systems.

## Usage
[TODO]

## Changelog
Please see [releases](../../releases) for more information on what has changed recently.

## Testing
``` bash
$ ./gradlew check
```

## Documentation
``` bash
$ ./gradlew dokka
```

## Contributing
Please see [CONTRIBUTING](CONTRIBUTING.md) for details.

## Security
If you discover any security-related issues, please email security@cafejojo.org instead of using the issue tracker.

## License
The MIT License (MIT). Please see the [license file](LICENSE) for more information.
