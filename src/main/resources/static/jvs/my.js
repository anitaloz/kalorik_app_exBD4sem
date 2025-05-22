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

    function toggleHidden(element) {
        element.classList.toggle('hidden');
    }

    breakfastButton.addEventListener('click', function() {
        //const out = this.closest('.meal-content').querySelector('.out');
        const out = this.closest('.meal').querySelector('.out');
        let isHidden = out.classList.contains('hidden');

        if (isHidden) {
            // Показываем форму
            formContainer1.innerHTML = formTemplate;

            const resultsContainer = out.querySelector('.results');
            fetch('getProducts')
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
                            const quantityInput = this.closest('tr').querySelector('#quantity');

                            //console.log(this.closest('tr'));

                            const quantity = parseFloat(quantityInput.value);
                            if (isNaN(quantity) || quantity <= 0) {
                                alert('Пожалуйста, введите корректное количество.');
                                return;
                            }
                            const l=document.getElementById('inputDate');
                            const dateString=l.value;
                            let inputDate=new Date(dateString);
                            const mealTitle='breakfast';
                            addProductToMeal(productId, mealTitle, quantity, productUnit, productSize, inputDate);  //Передаем unit
                        });
                    });
                })
                .catch(error => {
                    console.error('Ошибка:', error);
                    resultsContainer.innerHTML = '<p>Ошибка поиска.</p>';
                });
            out.classList.remove('hidden'); // Делаем видимым .out
            //out.querySelector('.results').style.display = 'none'; // Прячем результаты

            // Меняем текст кнопки на "Скрыть"
            breakfastButton.classList.add('minus');
            breakfastButton.classList.remove('plus');
            attachSearchHandler('formSearchingContainer1', '.results', 'breakfast');

        } else {
            out.classList.add('hidden');
            breakfastButton.classList.add('plus');
            breakfastButton.classList.remove('minus');
        }
    });


    document.getElementById('BreakfastShow').addEventListener('click', function() {
        //const out = this.closest('.meal-content').querySelector('.show');
        //const breakfastShowButton = document.getElementById('BreakfastShow');
        //console.log("CLICK");
        //console.log(this);
        //console.log(this.closest('.meal'));
        //console.log(this.closest('.meal').querySelector('.show'));
        const out = this.closest('.meal').querySelector('.show');

        const l = document.getElementById('inputDate');

        //console.log(out.classList.contains('hidden'));

        let isHidden = out.classList.contains('hidden');
        const dateString = l.value;
        if (isHidden) {
            const bfPrList = this.closest('.meal').querySelectorAll('.products-list');
            bfPrList.forEach(b => {
                b.style.display = 'none';
            });
            out.classList.remove('hidden');
            const el = this.closest('.meal').querySelector('.show');
            document.getElementById('BreakfastShow').classList.add('hid');
            document.getElementById('BreakfastShow').classList.remove('sh');
            showProducts('showContainerBF', 'breakfast', dateString);
        } else {
            out.classList.add('hidden');
            document.getElementById('BreakfastShow').classList.add('sh');
            document.getElementById('BreakfastShow').classList.remove('hid');
            const prList = this.closest('.meal').querySelectorAll('.products-list');
            prList.forEach(b=>{
                if(b.style.display==='none')
                    b.style.display='block';
            });
        }
    });

    document.getElementById('LunchShow').addEventListener('click', function() {
        //const out = this.closest('.meal-content').querySelector('.show');
        const out = this.closest('.meal').querySelector('.show');
        const l = document.getElementById('inputDate');
        let isHidden = out.classList.contains('hidden');
        const dateString = l.value;
        if (isHidden) {
            const bfPrList = this.closest('.meal').querySelectorAll('.products-list');
            bfPrList.forEach(b => {
                b.style.display = 'none';
            });
            out.classList.remove('hidden');
            document.getElementById('LunchShow').classList.add('hid');
            document.getElementById('LunchShow').classList.remove('sh');
            showProducts('showContainerL', 'lunch', dateString);
        } else {
            out.classList.add('hidden');
            document.getElementById('LunchShow').classList.add('sh');
            document.getElementById('LunchShow').classList.remove('hid');
            const prList = this.closest('.meal').querySelectorAll('.products-list');
            prList.forEach(b=>{
                if(b.style.display==='none')
                    b.style.display='block';
            });
        }
    });

    document.getElementById('DinnerShow').addEventListener('click', function() {
        //const out = this.closest('.meal-content').querySelector('.show');
        const out = this.closest('.meal').querySelector('.show');
        const l = document.getElementById('inputDate');
        let isHidden = out.classList.contains('hidden');
        const dateString = l.value;
        if (isHidden) {
            const bfPrList = this.closest('.meal').querySelectorAll('.products-list');
            bfPrList.forEach(b => {
                b.style.display = 'none';
            });
            out.classList.remove('hidden');
            document.getElementById('DinnerShow').classList.add('hid');
            document.getElementById('DinnerShow').classList.remove('sh');
            showProducts('showContainerD', 'dinner', dateString);
        } else {
            out.classList.add('hidden');
            document.getElementById('DinnerShow').classList.add('sh');
            document.getElementById('DinnerShow').classList.remove('hid');
            const prList = this.closest('.meal').querySelectorAll('.products-list');
            prList.forEach(b=>{
                if(b.style.display==='none')
                    b.style.display='block';
            });
        }
    });

    // dinnerButton.addEventListener('click', function() {
    //     const out = this.closest('.meal-content').querySelector('.out');
    //     let isHidden = out.classList.contains('hidden');
    //
    //     if (isHidden) {
    //         formContainer3.innerHTML = formTemplate;
    //         out.classList.remove('hidden');
    //         out.querySelector('.results').style.display = 'none';
    //         dinnerButton.textContent = 'Скрыть';
    //         attachSearchHandler('formSearchingContainer3', '.results', 'dinner');
    //
    //     } else {
    //         out.classList.add('hidden');
    //         dinnerButton.textContent = 'Добавить';
    //     }
    // });



    lunchButton.addEventListener('click', function() {
        //const out = this.closest('.meal-content').querySelector('.out');
        const out = this.closest('.meal').querySelector('.out');
        let isHidden = out.classList.contains('hidden');

        if (isHidden) {
            formContainer2.innerHTML = formTemplate;
            const resultsContainer = out.querySelector('.results');
            fetch('getProducts')
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
                            const quantityInput = this.closest('tr').querySelector('#quantity');

                            //console.log(this.closest('tr'));

                            const quantity = parseFloat(quantityInput.value);
                            if (isNaN(quantity) || quantity <= 0) {
                                alert('Пожалуйста, введите корректное количество.');
                                return;
                            }
                            const l=document.getElementById('inputDate');
                            const dateString=l.value;
                            let inputDate=new Date(dateString);
                            const mealTitle='lunch';
                            addProductToMeal(productId, mealTitle, quantity, productUnit, productSize, inputDate);  //Передаем unit
                        });
                    });
                })
                .catch(error => {
                    console.error('Ошибка:', error);
                    resultsContainer.innerHTML = '<p>Ошибка поиска.</p>';
                });

            out.classList.remove('hidden');
            lunchButton.classList.add('minus');
            lunchButton.classList.remove('plus');
            attachSearchHandler('formSearchingContainer2', '.results', 'lunch');

        } else {
            out.classList.add('hidden');
            lunchButton.classList.add('plus');
            lunchButton.classList.remove('minus');
        }
    });


    dinnerButton.addEventListener('click', function() {
        //const out = this.closest('.meal-content').querySelector('.out');
        const out = this.closest('.meal').querySelector('.out');;
        let isHidden = out.classList.contains('hidden');

        if (isHidden) {
            formContainer3.innerHTML = formTemplate;
            const resultsContainer = out.querySelector('.results');
            fetch('getProducts')
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
                            const quantityInput = this.closest('tr').querySelector('#quantity');

                            //console.log(this.closest('tr'));

                            const quantity = parseFloat(quantityInput.value);
                            if (isNaN(quantity) || quantity <= 0) {
                                alert('Пожалуйста, введите корректное количество.');
                                return;
                            }
                            const l=document.getElementById('inputDate');
                            const dateString=l.value;
                            let inputDate=new Date(dateString);
                            const mealTitle='dinner';
                            addProductToMeal(productId, mealTitle, quantity, productUnit, productSize, inputDate);  //Передаем unit
                        });
                    });
                })
                .catch(error => {
                    console.error('Ошибка:', error);
                    resultsContainer.innerHTML = '<p>Ошибка поиска.</p>';
                });
            out.classList.remove('hidden');
            dinnerButton.classList.add('minus');
            dinnerButton.classList.remove('plus');
            attachSearchHandler('formSearchingContainer3', '.results', 'dinner');

        } else {
            out.classList.add('hidden');
            dinnerButton.classList.add('plus');
            dinnerButton.classList.remove('minus');
        }
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
                        const quantityInput = this.closest('tr').querySelector('#quantity');

                        //console.log(this.closest('tr'));

                        const quantity = parseFloat(quantityInput.value);
                        if (isNaN(quantity) || quantity <= 0) {
                            alert('Пожалуйста, введите корректное количество.');
                            return;
                        }
                        const l=document.getElementById('inputDate');
                        const dateString=l.value;
                        let inputDate=new Date(dateString);
                        addProductToMeal(productId, mealTitle, quantity, productUnit, productSize, inputDate);  //Передаем unit
                    });
                });

                // const hideButtons = resultsContainer.querySelectorAll('.hideProductButton');
                // hideButtons.forEach(button => {
                //     button.addEventListener('click', function(event) {
                //         event.preventDefault();
                //
                //         // Находим родительский элемент 'out' кнопки.  Предполагается, что кнопка hide находится внутри формы или контейнера 'out'.
                //         const outhide = button.closest('.out'); // Находим ближайшего родителя с классом 'out'
                //         if (outhide) {
                //             outhide.style.display = 'none';
                //         }
                //     });
                // });


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
            return;
        }

        const params = new URLSearchParams({
            mealDate: mealDate,
            mealTitle: mealTitle
        });

        const url = '/getMealProducts?' + params.toString();

        console.log(`Fetching data from URL: ${url}`);

        fetch(url)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Network response was not ok: ${response.status} ${response.statusText}`);
                }
                return response.json();
            })
            .then(data => {
                console.log('Data received from server:', data);

                if (data && data.mealProducts) {
                    const mealProducts = data.mealProducts;

                    if (Array.isArray(mealProducts) && mealProducts.length >= 0) {
                        try {

                            const processedMealProducts = mealProducts.map(product => {
                                const calculatedCalories = (product.calories / product.servingsize) * product.quantity;
                                const calculatedBel = (product.bel / product.servingsize) * product.quantity;
                                const calculatedCh = (product.ch/ product.servingsize) * product.quantity;
                                const calculatedFats = (product.fats / product.servingsize) * product.quantity;
                                return {
                                    ...product, // Копируем существующие свойства
                                    calculatedCalories: calculatedCalories.toFixed(0),
                                    calculatedBel: calculatedBel.toFixed(1),
                                    calculatedCh: calculatedCh.toFixed(1),
                                    calculatedFats: calculatedFats.toFixed(1)
                                };
                            });


                            const template = `
                <h3>Съедено</h3>
                <table class="product-table">
                <thead><tr>
                <th>Продукт</th>
                <th>Кол-во</th>
                <th>К</th>
                <th>Б</th>
                <th>Ж</th>
                <th>У</th>
                <th></th>
                </tr></thead>
                <tbody>
                {{#mealProducts}}

                    <tr class="meal-item" data-product-id="{{id}}">
                        <td><span>{{name}}</span></td>
                        <td><span>{{quantity}} {{servingunit}}</span></td>
                        <td>{{calculatedCalories}}</td>
                        <td>{{calculatedBel}}</td>
                        <td>{{calculatedFats}}</td>
                        <td>{{calculatedCh}}</td>
                        <td><button class="deleteProductButton" data-product-id="{{id}}"></button></td>
                    </tr>
                {{/mealProducts}}
                </tbody>
                </table>
                {{^mealProducts}}
                    <p class="no-meals-data">Нет данных для приема пищи.</p>
                {{/mealProducts}}
            `;

                            // **Передаем обработанные данные в Mustache:**
                            const html = Mustache.render(template, { mealProducts: processedMealProducts });
                            resultsContainer.innerHTML = html;

                            // Обработчики событий
                            attachEventHandlers(resultsContainer, mealTitle, mealDate);


                        } catch (mustacheError) {
                            console.error('Mustache rendering error:', mustacheError);
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


    function attachEventHandlers(resultsContainer, mealTitle, mealDate) {
        // const hideButtons = resultsContainer.querySelectorAll('.hideShowContButton');
        // hideButtons.forEach(button => {
        //     button.addEventListener('click', function (event) {
        //         event.preventDefault();
        //         const outhide = button.closest('.show');
        //         if (outhide) {
        //             outhide.style.display = 'none';
        //         }
        //         const prList = button.closest('.meal-content').querySelectorAll('.products-list');
        //         prList.forEach(b=>{
        //             if(b.style.display==='none')
        //                 b.style.display='block';
        //         });
        //     });
        // });

        const deleteButtons = resultsContainer.querySelectorAll('.deleteProductButton');
        deleteButtons.forEach(button => {
            button.addEventListener('click', function (event) {
                event.preventDefault();
                const productId = this.dataset.productId;
                deleteProduct(productId, mealTitle, mealDate);
            });
        });
    }


    function deleteProduct(productId, mealTitle, mealDate) {
        fetch('/deleteProductFromMeal', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                productId: productId,
                mealTitle: mealTitle,
                mealDate: mealDate
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                console.log('Продукт удален');
                window.location.reload();
            })
            .catch(error => {
                console.error('Ошибка удаления продукта:', error);
                alert('Ошибка удаления продукта из приема пищи.');
            });
    }

    function addProductToMeal(productId, mealTitle, quantity, unit, productSize, inputDate) {
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
                productSize: productSize,
                inputDate: inputDate
            })
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                console.log('Продукт добавлен в прием пищи');
                window.location.reload();

            })
            .catch(error => {
                console.error('Ошибка добавления продукта:', error);
                alert('Ошибка добавления продукта в прием пищи.');
            });
    }



    const plusButton=document.getElementById("plus");
    const minusButton=document.getElementById("minus");
    const currentWeightInput = document.querySelector('.currentWeight');

    minusButton.addEventListener('click', function() {
        let currentWeight = parseFloat(currentWeightInput.value);
        currentWeight -= 0.1;
        currentWeightInput.value = currentWeight.toFixed(1); // Ограничить одним десятичным знаком
    });

    plusButton.addEventListener('click', function() {
        let currentWeight = parseFloat(currentWeightInput.value);
        currentWeight += 0.1;
        currentWeightInput.value = currentWeight.toFixed(1); // Ограничить одним десятичным знаком
    });



});




document.addEventListener('DOMContentLoaded', function() {
    flatpickr("#inputDate", {
      dateFormat: "Y-m-d", // Формат даты
      // Другие опции:
      // enableTime: true,  // Включить выбор времени
      // ...
    });
  });