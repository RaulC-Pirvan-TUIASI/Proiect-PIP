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
/*
function showMessage(message) {
    var messageBox = document.getElementById('messageBox');
    messageBox.textContent = message;
}
*/
var dropArea = document.getElementById('dropArea');
dropArea.addEventListener('dragover', handleDragOver);
dropArea.addEventListener('drop', handleDrop);

/*
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
            })
            .catch(error => {
                // Handle network errors
                console.error('Error:', error);
            });
    });

    // Disable message input
    messageInput.disabled = true;
});
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
                // Update the HTML with the server response
                showMessage(data);
            })
            .catch(error => {
                // Handle network errors
                console.error('Error:', error);
            });
    });

    // Disable message input
    messageInput.disabled = true;
});

*/
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
                // Update the HTML with the server response
                showMessage(data);
            })
            .catch(error => {
                // Handle network errors
                console.error('Error:', error);
            });

        // Disable message input
        messageInput.disabled = true;
    });
});


function showMessage(message) {
    var answerElement = document.getElementById('messageInput');
    if (answerElement) {
        // Update the answer element with the received message
        answerElement.textContent = message;
    } else {
        console.error('Answer element not found');
    }
}

function scrollToElement(elementSelector, instance = 0) {
    const elements = document.querySelectorAll(elementSelector);
    if(elements.length > instance) {
        elements[instance].scrollIntoView({behavior: 'smooth'});
    } 
}

const homeLink = document.getElementById("homeLink");
const teamLink = document.getElementById("teamLink");
const infoLink = document.getElementById("infoLink");
const contactLink = document.getElementById("contactLink");

homeLink.addEventListener('click', () => {
    event.preventDefault();
    scrollToElement('.main');
})

teamLink.addEventListener('click', () => {
    event.preventDefault();
    scrollToElement('.team-section');
})

infoLink.addEventListener('click', () => {
    event.preventDefault();
    scrollToElement('.info');
})

contactLink.addEventListener('click', () => {
    event.preventDefault();
    scrollToElement('.contact');
})

function refreshPage() {
    location.reload();
}

console.log("Current window width:", window.innerWidth);