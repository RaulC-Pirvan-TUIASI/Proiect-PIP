function previewImage(event) {
    var image = document.getElementById('preview');
    var imagePreview = document.getElementById('imagePreview');
    var dropAreaText = document.getElementById('dropAreaText');
    var chooseFileButton = document.querySelector('.custom-file-upload'); // Select the button by class

    // Hide the drop area text and choose file button
    // Test
    dropAreaText.style.display = 'none';
    chooseFileButton.style.display = 'none';

    // Set the image source to the uploaded file
    var reader = new FileReader();
    reader.onload = function(e) {
        image.src = e.target.result;
    }
    reader.readAsDataURL(event.target.files[0]);

    // Show the image preview
    imagePreview.classList.remove('hidden');

    // Adjust the height of the drop area (optional)
    document.getElementById('dropArea').style.height = '500px'; // Adjust the height as needed
}
