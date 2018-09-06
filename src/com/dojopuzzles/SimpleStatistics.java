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
    result.put("AMPLITUDE", String.valueOf(maxValue-minValue));// A diferen�a entre o maior e o menor item da sele��o
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
 * A moda mostra quais os valores mais aparecem na sele��o.
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
 * A vari�ncia, uma medida de dispers�o que mostra qu�o distantes os valores est�o da m�dia.
 * O c�lculo da vari�ncia populacional � obtido atrav�s da soma dos quadrados da diferen�a entre cada valor e a m�dia aritm�tica, dividida pela quantidade de elementos observados
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
   * Desvio padr�o � o resultado positivo da raiz quadrada da vari�ncia. 
   * Na pr�tica, o desvio padr�o indica qual � o �erro� se quis�ssemos substituir um dos valores coletados pelo valor da m�dia
   * Quanto maior o desvio, mais dispersos est�o os valores da sele��o
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
 * O primeiro quartil representa os 25% iniciais da sele��o que se exluido elimina os valores atipicos inferiores da sele��o (Sempre arredondados para cima)
 */
  private static int firstQuartile(Long size) {
    return (int) Math.ceil(size.doubleValue()/4) -1;
  }

  /**
   * 
   * @param size
   * @return end of third quartile
   * 
   * O terceiro quartil representa os 25% finais da sele��o que se exluido elimina os valores atipicos inferiores da sele��o (Sempre arredondados para cima)
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
 * Os outliers s�o valores muito diferentes do resto da sele��o tanto por serem muito altos, tanto por serem muito baixos.
 * Remove-los torna as medidas sobre a sele��o mais confiaveis
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
   * A m�dia aritimetia � calculada a partir da soma de todos os valores da cole��o, dividida pelo numero de itens da cole��o.
   * Em uma sele��o sem outliers, representa o valor medio dos itens
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
   * Mediana � o valor que aparece no meio da sele��o. Caso a sele��o tenha um numero de itens par, a mediana � a media aritimetica da soma dos dois valores medianos
   * Geralmente � uma valor mais confiavel que a m�dia, pois � menos sensivel aos outliers.
   * Quando a media e a mediana est�o muito distantes, significa que existe uma dispers�o grande nos valores da sele��o
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