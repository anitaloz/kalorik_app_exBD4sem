
    document.addEventListener('DOMContentLoaded', function() {
    document.body.addEventListener('submit', function(event) {
        // Проверяем, что событие произошло именно на форме с нужным id
        if (event.target && event.target.id === 'addForm') {
            let errors = [];
            const form = event.target;
            const name = form.elements['name'].value.trim();
            const calories = form.elements['calories'].value.trim();
            const bel = form.elements['bel'].value.trim();
            const fats = form.elements['fats'].value.trim();
            const ch = form.elements['ch'].value.trim();
            const servingSize = form.elements['servingSize'].value.trim();
            const servingUnit = form.elements['servingUnit'].value;

            // Валидация
            if (!name) errors.push('Введите название продукта');
            if (!calories || isNaN(calories) || Number(calories) <= 0) errors.push('Введите правильное значение калорийности.');
            if (!bel || isNaN(bel) || Number(bel) < 0) errors.push('Введите правильное значение белков.');
            if (!fats || isNaN(fats) || Number(fats) < 0) errors.push('Введите правильное значение жиров.');
            if (!ch || isNaN(ch) || Number(ch) < 0) errors.push('Введите правильное значение углеводов.');
            if (!servingSize || isNaN(servingSize) || Number(servingSize) <= 0) errors.push('Введите правильный размер порции.');
            if (!servingUnit) errors.push('Пожалуйста, выберите единицу измерения.');

            if (errors.length > 0) {
                alert(errors.join('\n'));
                event.preventDefault();
            }
        }
    });
});