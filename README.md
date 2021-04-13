# Задание

## Общий вариант для всех: Устранение левой рекурсии.

**Определение**. Нетерминал <!-- $A$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=A"> КС-грамматики <!-- $G = (N, \Sigma, P, S)$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=G%20%3D%20(N%2C%20%5CSigma%2C%20P%2C%20S)"> называется рекурсивным, если <!-- $A \Rightarrow^+ \alpha A \beta$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=A%20%5CRightarrow%5E%2B%20%5Calpha%20A%20%5Cbeta">
для некоторых <!-- $\alpha$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=%5Calpha"> и <!-- $\beta$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=%5Cbeta">. Если <!-- $\alpha = \varepsilon$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=%5Calpha%20%3D%20%5Cvarepsilon">, то <!-- $A$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=A"> называется леворекурсивным. Аналогично, если <!-- $\beta = \varepsilon$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=%5Cbeta%20%3D%20%5Cvarepsilon">, то <!-- $A$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=A">
называется праворекурсивным. Грамматика, имеющая хотя бы один леворекурсивный нетерминал, называется
леворекурсивной. Аналогично определяется праворекурсивная грамматика. Грамматика, в которой все
нетерминалы, кроме, быть может, начального символа, рекурсивные, называется рекурсивной.

Некоторые из алгоритмов разбора не могут работать с леворекурсивными грамматиками. Можно показать,
что каждый КС-язык определяется хотя бы одной не леворекурсивной грамматикой.

Постройте программу, которая в качестве входа принимает приведенную КС-грамматику <!-- $G = (N, \Sigma, P, S)$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=G%20%3D%20(N%2C%20%5CSigma%2C%20P%2C%20S)"> и 
преобразует ее в эквивалентную КС-грамматику <!-- $G'$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=G'">без левой рекурсии.

*Указания*.

1. Проработать самостоятельно п. 4.3.3. и п. 4.3.4. \[2\].
2. Воспользоваться алгоритмом 2.13. При тестировании воспользоваться примером 2.27. \[1\].
3. Воспользоваться алгоритмами 4.8 и 4.10. При тестировании воспользоваться примерами 4.7., 4.9. и 4.11. \[2\].
4. Устранять надо не только непосредственную (immediate), но и косвенную (indirect) рекурсию. Этот 
вопрос подробно затронут в \[4\]. 
5. После устранения левой рекурсии можно применить левую факторизацию.

## Вариант 3. Преобразование в грамматику без <!-- $\varepsilon$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=%5Cvarepsilon">-правил.

**Определение**. Назовем КС-грамматику <!-- $G = (N, \Sigma, P, S)$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=G%20%3D%20(N%2C%20%5CSigma%2C%20P%2C%20S)"> грамматикой без <!-- $\varepsilon$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=%5Cvarepsilon">-правил (или неукорачивающей), 
если либо
1. <!-- $P$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=P"> не содержит <!-- $\varepsilon$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=%5Cvarepsilon">-правил, либо
2. есть точно одно <!-- $\varepsilon$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=%5Cvarepsilon">-правило <!-- $S \rightarrow \varepsilon$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=S%20%5Crightarrow%20%5Cvarepsilon"> и <!-- $S$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=S"> не встречается в правых частях остальных правил из <!-- $P$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=P">.

Постройте программу, которая в качестве входа принимает произвольную КС-грамматику <!-- $G = (N, \Sigma, P, S)$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=G%20%3D%20(N%2C%20%5CSigma%2C%20P%2C%20S)"> и 
преобразует ее в эквивалентную КС-грамматику <!-- $G' = (N', \Sigma', P', S')$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=G'%20%3D%20(N'%2C%20%5CSigma'%2C%20P'%2C%20S')"> без <!-- $\varepsilon$ --> <img style="transform: translateY(0.1em); background: white;" src="https://render.githubusercontent.com/render/math?math=%5Cvarepsilon">-правил.

*Указания*. Воспользоваться алгоритмом 2.10. \[1\]. При тестировании воспользоваться примером 2.23. и 
упражнением 2.4.11. \[1\].


# Рекомендуемая литература

1. АХО А., УЛЬМАН Дж. Теория синтаксического анализа, перевода и компиляции: В 2-х томах. Т.1.: 
Синтаксический анализ. - М.: Мир, 1978.
2. АХО А.В, ЛАМ М.С., СЕТИ Р., УЛЬМАН Дж.Д. Компиляторы: принципы, технологии и инструменты. –
М.: Вильямс, 2008.
3. БУНИНА Е.И., ГОЛУБКОВ А.Ю. Формальные языки и грамматики. Учебное пособие. – М.: Изд-во МГТУ 
им. Н.Э.Баумана, Москва, 2006. URL: <http://iu9.bmstu.ru/data/book/fl.pdf>
4. Eliminating left-recursion: three steps. URL: <http://www.d.umn.edu/~hudson/5641/l11m.pdf>