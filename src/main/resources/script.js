var uploadCount = 0;

function previewImage(event) {
    var image = document.getElementById('preview');
    var imagePreview = document.getElementById('imagePreview');
    var dropAreaText = document.getElementById('dropAreaText');
    var orText = document.getElementById('orText');
    var chooseFileButton = document.querySelector('.custom-file-upload'); // Select the button by class

    // Increment the upload count
    uploadCount++;

    // Hide the drop area text and choose file button
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
}

document.getElementById("currentYear").innerText = new Date().getFullYear();

// Function to handle file upload when a file is dropped
function handleDrop(event) {
    event.preventDefault();
    event.stopPropagation();

    var files = event.dataTransfer.files;
    if (files.length > 0) {
        // Call the previewImage function to display the dropped image
        previewImage({ target: { files: files } });

        // Use AJAX to upload the file to the server
        var formData = new FormData();
        formData.append('file', files[0]);

        var xhr = new XMLHttpRequest();
        xhr.open('POST', '/upload', true);
        xhr.onload = function () {
            if (xhr.status === 200) {
                showMessage('File uploaded successfully');
            } else {
                showMessage('File upload failed');
            }
        };
        xhr.send(formData);
    }
}

// Attach dragover and drop event listeners to the drop area
var dropArea = document.getElementById('dropArea');
dropArea.addEventListener('dragover', handleDragOver);
dropArea.addEventListener('drop', handleDrop);

function handleDragOver(event) {
    event.preventDefault();
    event.stopPropagation();
}

function showMessage(message) {
    var messageBox = document.getElementById('messageBox');
    messageBox.textContent = message;
}

var dropArea = document.getElementById('dropArea');
dropArea.addEventListener('dragover', handleDragOver);
dropArea.addEventListener('drop', handleDrop);
