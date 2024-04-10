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

function showMessage(message) {
    var messageBox = document.getElementById('messageBox');
    messageBox.textContent = message;
}

var dropArea = document.getElementById('dropArea');
dropArea.addEventListener('dragover', handleDragOver);
dropArea.addEventListener('drop', handleDrop);

document.addEventListener("DOMContentLoaded", function () {
    var generateButton = document.getElementById("generate-button");
    var messageInput = document.getElementById("messageInput");

    generateButton.addEventListener("click", function () {
        // Create a FormData object
        var formData = new FormData();
        // Append the uploaded file to the FormData object
        formData.append('file', document.getElementById('file-upload').files[0]);

        // Send the FormData object to the server using Fetch API
        fetch('/upload', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    // Handle successful upload
                    return response.text();
                } else {
                    // Handle upload errors
                    throw new Error('Upload failed');
                }
            })
            .then(data => {
                // Handle server response
                console.log(data);
                var uploadSection = document.getElementById('uploadSection');
                moveUpwards(uploadSection);
                var chatBox = document.getElementById('chatBox');
                fadeIn(chatBox);
            })
            .catch(error => {
                // Handle network errors
                console.error('Error:', error);
            });
    });

    function fadeIn(element){
        element.style.opacity = 0;
        element.classList.remove('hidden');
        var opacity = 0;
        var interval = setInterval(function() {
            if(opacity>=1){
                clearInterval(interval);
            } else{
                opacity += 0.03;
                element.style.opacity = opacity;
            }
        }, 1);
    }

    function moveUpwards(element){
        var marginTop = 200;
        var marginBottom = -150;
        var interval = setInterval(function() {
            if (marginTop <= 70) {
                clearInterval(interval);
            } else {
                marginBottom +=5;
                marginTop -= 5;
                element.style.marginTop = marginTop + 'px';
                element.style.marginBottom = marginBottom + 'px';
            }
        }, 5);
    }
    // Disable message input
    messageInput.disabled = true;
});
