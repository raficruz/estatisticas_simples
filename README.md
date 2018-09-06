# estatisticas_simples
Respondendo ao dojo puzzle da url:
http://dojopuzzles.com/problemas/exibe/calculando-estatisticas-simples/#

Tornei mais desafiante o problema e adicionei outros indicadores, a lista de valores atual contempla:

<ul style="list-style-type:disc">

<li>VALOR_MINIMO</li>
</br>
<li>VALOR_MAXIMO</li>
</br>
<li>TAMANHO_DA_COLECAO</li>
</br>
<li>AMPLITUDE (A distancia entre o menor e o maior item da coleção)</li>
</br>
<li>MEDIA_ABSOLUTA (Media simples que só funciona se nao houverem valores atipicos)
</br>
<li>MEDIA_SEM_OUTLIERS (A média aritimetica considerando apenas os 50% da coleção que se encontram no centro desta. Serve para se excluir valores atipicos e tornar a media mais proxima do ideal)</li>
</br>
<li>VALOR_MEDIANO (Aquele(s) valor(es) que se encontram no meio da seleção, que no caso de termos valores atípicos na coleção, costuma dar uma maior noção do valor medio desta)</li>
</br>
<li>MODA (Quais elementos mais se repetem na coleção)</li>
</br>
<li>VARIANCIA (O quão longe" em geral os seus valores se encontram do valor esperado)</li>
</br>
<li>DESVIO_PADRAO (Indica quanto os dados estão espalhados em uma coleção, quanto maior o valor mas espalhados estão os valores ao redor da media)</li>
</ul>
</br>
</br>
<p>Para testar, adicione como dependencia do seu projeto e chame o metodo estatico SimpleStatistics.calcEstatistics(String[] args) passando a lista de numeros como array de strings</p>
