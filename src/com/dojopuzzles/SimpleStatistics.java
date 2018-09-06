package com.dojopuzzles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class SimpleStatistics {

  public static void main(String[] args) {    
    Map<String, String> result = calcEstatistics(args);
    System.out.println(result);
  }

  public static Map<String, String> calcEstatistics(String[] args) {
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
    result.put("AMPLITUDE", String.valueOf(maxValue-minValue));// A diferença entre o maior e o menor item da seleção
    result.put("MEDIA_ABSOLUTA", String.valueOf(mean));
    result.put("MEDIA_SEM_OUTLIERS", String.valueOf(noOutliersMean));
    result.put("VALOR_MEDIANO", String.valueOf(median(sortedCollection)));
    result.put("MODA", String.valueOf(statmod));
    result.put("VARIANCIA", String.valueOf(variancy));
    result.put("DESVIO_PADRAO", String.valueOf(standardDeviation));
    return result;
  }

/**
 * @param sortedCollection
 * @return stat mod values
 * 
 * A moda mostra quais os valores mais aparecem na seleção.
 * 
 */
  private static List<String> statMod(List<Integer> sortedCollection) {
    Map<String, Long> occurencies = sortedCollection.stream().collect(Collectors.groupingBy(e -> e.toString(), Collectors.counting()));
    long mostOften = occurencies.values().stream().max(Comparator.naturalOrder()).get();
    
    return occurencies.entrySet().stream()
        .filter(e -> e.getValue() == mostOften)
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
  }

/**
 * 
 * @param sortedCollection
 * @param mean
 * @return Variance Value
 * 
 * A variância, uma medida de dispersão que mostra quão distantes os valores estão da média.
 * O cálculo da variância populacional é obtido através da soma dos quadrados da diferença entre cada valor e a média aritmética, dividida pela quantidade de elementos observados
 * 
 */
  private static double variance(List<Integer> sortedCollection, Double mean) {
    List<Double> deviations = new ArrayList<>();
    sortedCollection.forEach( i -> deviations.add(Math.pow((i-mean), 2)));
    return deviations.stream().mapToDouble(d->d).sum() / sortedCollection.size();
  }

  /**
   * 
   * @param variancy
   * @return standard deviation value
   * 
   * Desvio padrão é o resultado positivo da raiz quadrada da variância. 
   * Na prática, o desvio padrão indica qual é o “erro” se quiséssemos substituir um dos valores coletados pelo valor da média
   * Quanto maior o desvio, mais dispersos estão os valores da seleção
   * 
   */
  private static double standardDeviation(Double variancy) {
    return Math.sqrt(variancy);
  }

/**
 * 
 * @param size
 * @return end of first quartile
 * 
 * O primeiro quartil representa os 25% iniciais da seleção que se exluido elimina os valores atipicos inferiores da seleção (Sempre arredondados para cima)
 */
  private static int firstQuartile(Long size) {
    return (int) Math.ceil(size.doubleValue()/4) -1;
  }

  /**
   * 
   * @param size
   * @return end of third quartile
   * 
   * O terceiro quartil representa os 25% finais da seleção que se exluido elimina os valores atipicos inferiores da seleção (Sempre arredondados para cima)
   */
  private static int thirdQuartile(Long size) {
    return (int) Math.ceil(3*size.doubleValue()/4);
  }

/**
 * 
 * @param sortedCollection
 * @param size
 * @return A selection with no outliers
 * 
 * Os outliers são valores muito diferentes do resto da seleção tanto por serem muito altos, tanto por serem muito baixos.
 * Remove-los torna as medidas sobre a seleção mais confiaveis
 * 
 */
  private static List<Integer> removeOutliers(List<Integer> sortedCollection, Long size) {
    int firstQuartile =  firstQuartile(size);
    int thirdQuartile = thirdQuartile(size);
    return sortedCollection.subList(firstQuartile, thirdQuartile);
  }

  /**
   * 
   * @param sortedCollection
   * @return arithimetic mean value
   * 
   * A média aritimetia é calculada a partir da soma de todos os valores da coleção, dividida pelo numero de itens da coleção.
   * Em uma seleção sem outliers, representa o valor medio dos itens
   * 
   */
  private static double average(List<Integer> sortedCollection) {
    return sortedCollection.stream().mapToInt(v -> v).average().orElse(Double.NaN);
  }

  /**
   * 
   * @param sortedCollection
   * @return median value
   * 
   * Mediana é o valor que aparece no meio da seleção. Caso a seleção tenha um numero de itens par, a mediana é a media aritimetica da soma dos dois valores medianos
   * Geralmente é uma valor mais confiavel que a média, pois é menos sensivel aos outliers.
   * Quando a media e a mediana estão muito distantes, significa que existe uma dispersão grande nos valores da seleção
   * 
   */
  public static double median(List<Integer> sortedCollection) {
    int middleValue = sortedCollection.size() / 2;
    if (sortedCollection.size() % 2 == 1) {
      return sortedCollection.get(middleValue);
    } else {
      return (sortedCollection.get(middleValue - 1) + sortedCollection.get(middleValue)) / 2.0;
    }
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

}