function previewImage(event) {
    var image = document.getElementById('preview');
    var imagePreview = document.getElementById('imagePreview');
    var dropAreaText = document.getElementById('dropAreaText');
    var orText = document.getElementById('orText');
    var chooseFileButton = document.querySelector('.custom-file-upload'); // Select the button by class

    // Hide the drop area text and choose file button
    // Test
    dropAreaText.style.display = 'none';
    orText.style.display = 'none';
    chooseFileButton.style.display = 'none';

    // Set the image source to the uploaded file
    var reader = new FileReader();
    reader.onload = function (e) {
        image.src = e.target.result;
    }
    reader.readAsDataURL(event.target.files[0]);

    // Show the image preview
    imagePreview.classList.remove('hidden');

    dropArea.style.height = '300px';

    dropArea.classList.add('uploaded');

    // Adjust the height of the drop area (optional)
    document.getElementById('dropArea').style.height = '300px'; // Adjust the height as needed

    // Update current year
    document.getElementById("currentYear").innerText = new Date().getFullYear();
}

function handleDrop(event) {
    event.preventDefault();
    event.stopPropagation();

    var files = event.dataTransfer.files;
    if (files.length > 0) {
        previewImage({target: {files: files}});
    }
}

function handleDragOver(event) {
    event.preventDefault();
    event.stopPropagation();
}

var dropArea = document.getElementById('dropArea');
dropArea.addEventListener('dragover', handleDragOver);
dropArea.addEventListener('drop', handleDrop);