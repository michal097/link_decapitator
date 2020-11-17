function getFile() {
    document.getElementById("upfile").click();
}

function sub(obj) {
    const file = obj.value;
    const fileName = file.split("\\");
    document.getElementById("yourBtn").innerHTML = fileName[fileName.length - 1];

}
