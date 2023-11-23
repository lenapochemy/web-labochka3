var canvasDrawer;
window.onload = function (){
    var canvas = document.getElementById("canvas");
    canvasDrawer = new CanvasDrawer(canvas);
    canvasDrawer.lastR = sessionStorage.getItem("lastR");
    if(canvasDrawer.lastR == null) canvasDrawer.lastR = 1;
    canvasDrawer.drawArea(canvasDrawer.lastR);
    //setParamR();
    canvasDrawer.canvas.addEventListener('click', function (event){
        canvasDrawer.clickDot(event)
    });
}