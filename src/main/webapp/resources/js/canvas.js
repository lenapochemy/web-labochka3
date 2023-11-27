var canvasDrawer;
window.onload = function (){
    var canvas = document.getElementById("canvas");
    canvasDrawer = new CanvasDrawer(canvas);
    let r = sessionStorage.getItem("lastR");
    if(r == null) r = 1;
    canvasDrawer.drawArea(r);
    //setParamR();
    canvasDrawer.canvas.addEventListener('click', function (event){
        canvasDrawer.clickDot(event)
    });
}