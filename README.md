# Quizzer

A ready to use Java-FX app for self-generating multiple choice tests out of a question pool stored in a GitHub repository.

## How to use

1. Generate a repository with the prefix `questions.` on GitHub and upload each question in a separate .yml file following the correct [yaml format](http://yaml.org) explained below.
2. From the app, check out the repository. Once download finishes, you will be able to store the necessary info offline in order to exercise yourself even without an internet connection.

## Question format

The question has to contain:

- The question
- A list of possible answers
- The index of the (unique) right answer expressed in character form (starting by A)

A Java object should look like this:

``` java
class Data {
    String question;
    List<String> answers;
    char correct;

    ...
}
```

Which then have to be stored in a .yml file following the [yaml format](http://yaml.org).

Some examples:

``` yml
question: 1 + 2
answers:
- 2
- -1
- 3
- 4
correct: C
```

``` yml
question: |-
  int unknown(int a) {
      return a * 2;
  }

  What's the result of unknown(3)?
answers:
- None of the other answers
- 3
- 10
- 5
- 0
correct: A
```

## Credits

The idea of the app was born from the [mfranzil](https://github.com/mfranzil) project: [RispondiDomande](https://github.com/mfranzil/RispondiDomande)

## Authors

- **Luca Mosetti** - _Initial work_ - [gekoramy](https://github.com/gekoramy)
