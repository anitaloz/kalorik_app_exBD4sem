document.addEventListener("DOMContentLoaded", function() {
    const breakfastButton = document.getElementById('BreakfastAdd');
    //const breakfastShowButton = document.getElementById('BreakfastShow');
    const lunchButton = document.getElementById('LunchAdd');
    //const lunchShowButton = document.getElementById('LunchShow');
    const dinnerButton = document.getElementById('DinnerAdd');
    //const dinnerShowButton = document.getElementById('DinnerShow');

    const formContainer1 = document.getElementById('formSearchingContainer1');
    const formContainer2 = document.getElementById('formSearchingContainer2');
    const formContainer3 = document.getElementById('formSearchingContainer3');
    const showContainerD = document.getElementById('showContainerD');
    const showContainerL = document.getElementById('showContainerL');
    const showContainerBF = document.getElementById('showContainerBF');

    const breakfastResults = document.getElementById('breakfastResults');

    const outputContainer = document.getElementById('outputContainer');

    // Шаблон формы поиска
    const formTemplate = `
            <form name="searchForm" id=mealTitle method="get" action="">
                <input type="text" name="filter">
                <button type="submit">Найти</button>
            </form>
        `;


    //КНОПКИ
    breakfastButton.addEventListener('click', () => {
        formContainer1.innerHTML = formTemplate;
        const outh = formContainer1.closest('.out');
        if (outh.style.display === 'none') {
            outh.style.display = 'block';
            outh.querySelector('.results').style.display='none';
        }
        attachSearchHandler('formSearchingContainer1', '.results', 'breakfast');
    });

    document.getElementById('BreakfastShow').addEventListener('click', () => {
        let currentDate = new Date();
        let formattedDate = currentDate.toLocaleDateString('en-CA'); // 'en-CA' для YYYY-MM-DD
        const outh = showContainerBF.closest('.show');
        if (outh.style.display === 'none')
            outh.style.display = 'block';
        showProducts('showContainerBF', 'breakfast', formattedDate);
    });

    document.getElementById('LunchShow').addEventListener('click', () => {
        let currentDate = new Date();
        let formattedDate = currentDate.toLocaleDateString('en-CA'); // 'en-CA' для YYYY-MM-DD
        const outh = showContainerL.closest('.show');
        if (outh.style.display === 'none')
            outh.style.display = 'block';
        showProducts('showContainerL', 'lunch', formattedDate);
    });

    document.getElementById('DinnerShow').addEventListener('click', () => {
        let currentDate = new Date();
        let formattedDate = currentDate.toLocaleDateString('en-CA'); // 'en-CA' для YYYY-MM-DD
        const outh = showContainerD.closest('.show');
        if (outh.style.display === 'none') {
            outh.style.display = 'block';
            breakfastResults.style.display='none';
        }
        showProducts('showContainerD', 'dinner', formattedDate);
    });

    lunchButton.addEventListener('click', () => {
        formContainer2.innerHTML = formTemplate;
        const outh = formContainer2.closest('.out');
        if (outh.style.display === 'none') {
            outh.style.display = 'block';
            outh.querySelector('.results').style.display='none';
        }
        attachSearchHandler('formSearchingContainer2','.results', 'lunch');
    });

    dinnerButton.addEventListener('click', () => {
        formContainer3.innerHTML = formTemplate;
        const outh = formContainer3.closest('.out');
        if (outh.style.display === 'none') {
            outh.style.display = 'block';
            outh.querySelector('.results').style.display='none';
        }
        attachSearchHandler('formSearchingContainer3', '.results', 'dinner');
    });


    //ФУНКЦИИ
    function attachSearchHandler(formContainerId, resultsContainerId, mealTitle) {
        const formContainer = document.getElementById(formContainerId);
        formContainer.addEventListener('submit', function(event) {
            const formContainer = document.getElementById(formContainerId);
            const outh = formContainer.closest('.out');
            const resultsContainer = outh.querySelector('.results');
            if (resultsContainer.style.display==='none') {
                resultsContainer.style.display = 'block';
            }
            event.preventDefault();//Предотвращает стандартное поведение формы (которое заключается в перезагрузке страницы).
            const form = event.target;// получает HTML элемент формы
            const filterValue = form.querySelector('input[name="filter"]').value;//Получает значение, введенное пользователем в поле ввода с name="filter". Это значение используется для поиска продуктов.
            searchProducts(filterValue, resultsContainerId, formContainerId, mealTitle);
        });
    }



//     function searchProducts(filterValue, resultsContainerId, mealTitle) { ... }:  Эта функция выполняет поиск продуктов на сервере и отображает результаты.
//     •   const resultsContainer = document.getElementById(resultsContainerId);:  Получает элемент HTML контейнера результатов.
//     •   fetch('/getProducts?filter=' + filterValue):  Отправляет GET-запрос на URL /getProducts с параметром filter.  Этот запрос отправляет значение фильтра на сервер.
//     •   .then(response => { ... }):  Обрабатывает ответ от сервера.
//     *   if (!response.ok) { throw new Error('Network response was not ok'); }:  Проверяет, успешно ли выполнен запрос.  Если нет, выбрасывается ошибка.
//     *   return response.text();:  Получает текст ответа (HTML-код списка продуктов).
// •   .then(html => { ... }):  Обрабатывает HTML-код списка продуктов.
//     *   resultsContainer.innerHTML = html;:  Вставляет HTML-код в контейнер результатов.  Это отображает список продуктов на странице.
//     *   const addButtons = resultsContainer.querySelectorAll('.addProductButton');:  Получает все кнопки с классом .addProductButton (кнопки "Добавить" рядом с продуктами).
// *   addButtons.forEach(button => { ... });:  Для каждой кнопки "Добавить" добавляет обработчик события click.
//     *   button.addEventListener('click', function(event) { ... });:  Добавляет обработчик события click к кнопке.
//     *   event.preventDefault();:  Предотвращает стандартное поведение кнопки (которое могло бы вызвать перезагрузку страницы).
// *   const productId = this.getAttribute('data-product-id');:  Получает значение атрибута data-product-id кнопки.  Этот атрибут содержит ID продукта, который нужно добавить.
//     *   addToMeal(productId, mealTitle);:  Вызывает функцию addToMeal, передавая ID продукта и тип приема пищи.  Эта функция должна отправлять запрос на сервер для добавления продукта в базу данных.
//     •   .catch(error => { ... }):  Обрабатывает ошибки, которые могут возникнуть при выполнении запроса.


    function searchProducts(filterValue, resultsContainerId, formContainerId, mealTitle) {
        const formContainer = document.getElementById(formContainerId);
        const outh = formContainer.closest('.out');
        const resultsContainer = outh.querySelector('.results');

        fetch('/getProducts?filter=' + filterValue)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.text();
            })
            .then(html => {
                resultsContainer.innerHTML = html;

                const addButtons = resultsContainer.querySelectorAll('.addProductButton');
                addButtons.forEach(button => {
                    button.addEventListener('click', function(event) {
                        event.preventDefault();

                        const productId = this.getAttribute('data-product-id');
                        const productUnit = this.getAttribute('data-product-unit');
                        const productSize = this.getAttribute('data-product-size');

                        // Находим input для количества внутри родительского элемента кнопки
                        const quantityInput = this.parentNode.querySelector('#quantity');
                        const quantity = parseFloat(quantityInput.value);
                        if (isNaN(quantity) || quantity <= 0) {
                            alert('Пожалуйста, введите корректное количество.');
                            return;
                        }

                        addProductToMeal(productId, mealTitle, quantity, productUnit, productSize);  //Передаем unit
                    });
                });

                const hideButtons = resultsContainer.querySelectorAll('.hideProductButton');
                hideButtons.forEach(button => {
                    button.addEventListener('click', function(event) {
                        event.preventDefault();

                        // Находим родительский элемент 'out' кнопки.  Предполагается, что кнопка hide находится внутри формы или контейнера 'out'.
                        const outhide = button.closest('.out'); // Находим ближайшего родителя с классом 'out'
                        if (outhide) {
                            outhide.style.display = 'none';
                        }
                    });
                });


            })
            .catch(error => {
                console.error('Ошибка:', error);
                resultsContainer.innerHTML = '<p>Ошибка поиска.</p>';
            });
    }

    function showProducts(resultsContainerId, mealTitle, mealDate) {
        const resultsContainer = document.getElementById(resultsContainerId);

        if (!resultsContainer) {
            console.error(`Error: Results container with ID '${resultsContainerId}' not found in the DOM.`);
            return; // Exit the function if the container is missing
        }

        const params = new URLSearchParams({
            mealDate: mealDate,
            mealTitle: mealTitle
        });

        const url = '/getMealProducts?' + params.toString();

        console.log(`Fetching data from URL: ${url}`); // Log the URL for debugging

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Network response was not ok: ${response.status} ${response.statusText}`); // Include status in error
                }
                return response.json();
            })
            .then(data => {
                console.log('Data received from server:', data); // Log the entire data object

                if (data && data.mealProducts) {
                    const mealProducts = data.mealProducts;

                    if (Array.isArray(mealProducts) && mealProducts.length >= 0) {  // Check if it's an array
                        try {
                            const template = `
                                <h3>Съедено</h3>
                                {{#mealProducts}}
                                    <div>
                                        <span>{{name}}</span> - <span>{{quantity}} {{unit}}</span><br>
                                        <i>Калории: {{calories}}</i>,
                                        <i>Белки: {{bel}}</i>,
                                        <i>Жиры: {{fats}}</i>,
                                        <i>Углеводы: {{ch}}</i>
                                    </div>
                                {{/mealProducts}}
                                {{^mealProducts}}
                                    <p>Нет данных для приема пищи.</p>
                                {{/mealProducts}}
                                <button class="hideShowContButton">Скрыть</button>
                        `;
                            const html = Mustache.render(template, { mealProducts: mealProducts });
                            resultsContainer.innerHTML = html;


                            const hideButtons = resultsContainer.querySelectorAll('.hideShowContButton');
                            hideButtons.forEach(button => {
                                button.addEventListener('click', function(event) {
                                    event.preventDefault();

                                    // Находим родительский элемент 'out' кнопки.  Предполагается, что кнопка hide находится внутри формы или контейнера 'out'.
                                    const outhide = button.closest('.show'); // Находим ближайшего родителя с классом 'out'
                                    if (outhide) {
                                        outhide.style.display = 'none';
                                    }
                                });
                            });
                        } catch (mustacheError) {
                            console.error('Mustache rendering error:', mustacheError); // Log the Mustache error specifically
                            resultsContainer.innerHTML = '<p>Ошибка при рендеринге шаблона.</p>';
                        }

                    } else {
                        resultsContainer.innerHTML = '<p>Нет продуктов для данного приема пищи.</p>';
                    }
                } else {
                    resultsContainer.innerHTML = '<p>Ошибка: Неверный формат данных от сервера.</p>';
                }
            })
            .catch(error => {
                console.error('Fetch error:', error);
                resultsContainer.innerHTML = '<p>Ошибка получения продуктов.</p>';
            });
    }





    function addProductToMeal(productId, mealTitle, quantity, unit, productSize) {
        // Отправляем данные на сервер
        fetch('/addProductToMeal', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                productId: productId,
                mealTitle: mealTitle,
                quantity: quantity,
                unit: unit,
                productSize: productSize
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                console.log('Продукт добавлен в прием пищи');
                const addButton = document.querySelector(`.addProductButton[data-product-id="${productId}"]`); // находим кнопку
                const addedMessage = addButton.nextElementSibling; // Берем следующий элемент (span)

                //  Отображаем сообщение
                addedMessage.style.display = 'inline';

                //  Скрываем сообщение через 3 секунды (опционально)
                setTimeout(() => {
                    addedMessage.style.display = 'none';
                }, 3000);
            })
            .catch(error => {
                console.error('Ошибка добавления продукта:', error);
                alert('Ошибка добавления продукта в прием пищи.');
            });
    }
});