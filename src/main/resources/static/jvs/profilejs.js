document.addEventListener("DOMContentLoaded", function() {

    console.log("check");

    //-----------------------------------------
    document.getElementById('coverImageInput').addEventListener('change', function() {
        var fileName = this.files[0].name;

        console.log(document.getElementById('file-name'));
        console.log(document.getElementById('file-name').textContent);

        document.getElementById('file-name').textContent = fileName;
    });

});


document.addEventListener('DOMContentLoaded', function() {
     flatpickr("#dateOfBirth", {
       dateFormat: "Y-m-d", // Формат даты
       // Другие опции:
       // enableTime: true,  // Включить выбор времени
       // ...
     });
   });