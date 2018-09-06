package com.dojopuzzles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class Main {

  public static void main(String[] args) {    
    List<Integer> sortedCollection = Arrays.stream(args).map(Integer::valueOf).collect(Collectors.toList());
    sortedCollection.sort((s1,s2) -> Integer.compare(Integer.valueOf(s1), Integer.valueOf(s2)));

    Integer minValue = minValue(sortedCollection);
    Integer maxValue = maxValue(sortedCollection);
    Long size = size(sortedCollection);
    Double mean = average(sortedCollection);
    List<Integer> outliersFree = removeOutliers(sortedCollection, size);
    Double noOutliersMean = average(outliersFree);
    Double variancy = variance(sortedCollection, mean);
    Double standardDeviation = standardDeviation(variancy);
    List<String> statmod = statMod(sortedCollection);
    
    Map<String, String> result = new HashMap<>();
    result.put("VALOR_MINIMO", String.valueOf(minValue));
    result.put("VALOR_MAXIMO", String.valueOf(maxValue));
    result.put("TAMANHO_DA_COLECAO", String.valueOf(size));
    result.put("AMPLITUDE", String.valueOf(maxValue-minValue));
    result.put("MEDIA_ABSOLUTA", String.valueOf(mean));
    result.put("MEDIA_SEM_OUTLIERS", String.valueOf(noOutliersMean));
    result.put("VALOR_MEDIANO", String.valueOf(median(sortedCollection)));
    result.put("MODA", String.valueOf(statmod));
    result.put("VARIANCIA", String.valueOf(variancy));
    result.put("DESVIO_PADRAO", String.valueOf(standardDeviation));
    
    System.out.println(result);
  }

  private static double standardDeviation(Double variancy) {
    return Math.sqrt(variancy);
  }

  private static List<String> statMod(List<Integer> sortedCollection) {
    Map<String, Long> occurencies = sortedCollection.stream().collect(Collectors.groupingBy(e -> e.toString(), Collectors.counting()));
    long mostOften = occurencies.values().stream().max(Comparator.naturalOrder()).get();
    
    return occurencies.entrySet().stream()
        .filter(e -> e.getValue() == mostOften)
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
  }

  private static double variance(List<Integer> sortedCollection, Double mean) {
    List<Double> deviations = new ArrayList<>();
    sortedCollection.forEach( i -> deviations.add(Math.pow((i-mean), 2)));
    return deviations.stream().mapToDouble(d->d).sum() / sortedCollection.size();
  }

  private static List<Integer> removeOutliers(List<Integer> sortedCollection, Long size) {
    int firstQuartile =  firstQuartile(size);
    int thirdQuartile = thirdQuartile(size);
    return sortedCollection.subList(firstQuartile, thirdQuartile);
  }

  private static int thirdQuartile(Long size) {
    return (int) Math.ceil(3*size.doubleValue()/4);
  }

  private static int firstQuartile(Long size) {
    return (int) Math.ceil(size.doubleValue()/4) -1;
  }

  private static double average(List<Integer> sortedCollection) {
    return sortedCollection.stream().mapToInt(v -> v).average().orElse(Double.NaN);
  }

  private static long size(List<Integer> sortedCollection) {
    return sortedCollection.stream().mapToInt(v -> v).count();
  }

  private static int maxValue(List<Integer> sortedCollection) {
    return sortedCollection.stream().mapToInt(v -> v).max().orElseThrow(NoSuchElementException::new);
  }

  private static int minValue(List<Integer> sortedCollection) {
    return sortedCollection.stream().mapToInt(v -> v).min().orElseThrow(NoSuchElementException::new);
  }

  public static double median(List<Integer> sortedCollection) {
    int middleValue = sortedCollection.size() / 2;
    if (sortedCollection.size() % 2 == 1) {
      return sortedCollection.get(middleValue);
    } else {
      return (sortedCollection.get(middleValue - 1) + sortedCollection.get(middleValue)) / 2.0;
    }
  }
}