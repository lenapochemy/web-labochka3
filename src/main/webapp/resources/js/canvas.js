var canvasDrawer;
window.onload = function (){
    var canvas = document.getElementById("canvas");
    canvasDrawer = new CanvasDrawer(canvas);
    canvasDrawer.canvas.addEventListener('click', function (event){
        canvasDrawer.clickDot(event)
    });
}