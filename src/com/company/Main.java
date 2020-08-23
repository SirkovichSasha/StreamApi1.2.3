package com.company;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {

        List<String> names = Arrays.asList("Jack", "Connor", "Harry", "George", "Samuel", "John");
        List<String> families = Arrays.asList("Evans", "Young", "Harris", "Wilson", "Davies", "Adamson", "Brown");
        Collection<Person> persons = new ArrayList<>();
        for (int i = 0; i < 10_000_000; i++) {
            persons.add(new Person(
                    names.get(new Random().nextInt(names.size())),
                    families.get(new Random().nextInt(families.size())),
                    new Random().nextInt(100),
                    Sex.values()[new Random().nextInt(Sex.values().length)],
                    Education.values()[new Random().nextInt(Education.values().length)])
            );
        }
        calculate(persons);
        calculateParallel(persons);
    }
    
    private static void calculate(Collection<Person> persons) {
        long startTime = System.nanoTime();
        Stream<Person> stream = persons.stream();
        long count = stream.filter(x -> x.getAge() < 18).count();
        System.out.println("количество несовершеннолетних : " + count);

        Stream<Person> stream2 = persons.stream();
        stream2.filter(x -> x.getAge() > 17 && x.getAge() < 28).map(x -> x.getFamily()).collect(Collectors.toList()).forEach(System.out::println);

        Stream<Person> stream3 = persons.stream();
        stream3.filter(x -> x.getAge() > 17).filter(x -> (x.getAge() < 67 && x.getSex() == Sex.MAN) || (x.getAge() < 61 && x.getSex() == Sex.WOMEN))
                .filter(x -> x.getEducation() == Education.HIGHER)
                .sorted(Comparator.comparing(Person::getFamily))
                .collect(Collectors.toList())
                .forEach(System.out::println);
        long stopTime = System.nanoTime();
        double processTime = (double) (stopTime - startTime) / 1_000_000_000.0;
        System.out.println("Process time: " + processTime + " s");
    }

    private static void calculateParallel(Collection<Person> persons) {
        long startTime = System.nanoTime();
        Stream<Person> parallelStream = persons.stream();
        long count = parallelStream.filter(x -> x.getAge() < 18).count();
        System.out.println("количество несовершеннолетних : " + count);

        Stream<Person> parallelStream2 = persons.stream();
        parallelStream2.filter(x -> x.getAge() > 17 && x.getAge() < 28).map(x -> x.getFamily()).collect(Collectors.toList()).forEach(System.out::println);

        Stream<Person> parallelStream3 = persons.stream();
        parallelStream3.filter(x -> x.getAge() > 17).filter(x -> (x.getAge() < 67 && x.getSex() == Sex.MAN) || (x.getAge() < 61 && x.getSex() == Sex.WOMEN))
                .filter(x -> x.getEducation() == Education.HIGHER)
                .sorted(Comparator.comparing(Person::getFamily))
                .collect(Collectors.toList())
                .forEach(System.out::println);
        long stopTime = System.nanoTime();
        double processTime = (double) (stopTime - startTime) / 1_000_000_000.0;
        System.out.println("Process time: " + processTime + " s");
    }
}
