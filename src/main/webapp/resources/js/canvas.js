const canvas = document.getElementById("canvas");
const ctx = canvas.getContext("2d");
ctx.fillStyle = "deeppink";


window.onload = function (){
    drawAxes();
    let buttons = document.querySelectorAll("input[name=r]");
    buttons.forEach(click);
    function click(button){
        button.onclick = function (){
            r = this.value.replace(",", ".");
            drawArea(r);
        }
    }
}

/*canvas.addEventListener('click', function (event){
    if(r == null) createNot("Невозможно определить координаты, выберите сначала значение радиуса")
    else {
        let loc = windowToCanvas(canvas, event.clientX, event.clientY);
        let x = xFromCanvas(loc.x);
        let y = yFromCanvas(loc.y);
        sendToServer(x, y, r);
    }
});

 */

function drawAxes(){
    ctx.beginPath();
    ctx.moveTo(0, 250);
    ctx.lineTo(500, 250);
    ctx.moveTo(250, 0);
    ctx.lineTo(250, 500)
    ctx.stroke();
}

function xToCanvas(x){
    return (x * 50) + 250;
}
function yToCanvas(y){
    return 250 - (y * 50);
}
function xFromCanvas(x){
    return (x - 250)/50;
}
function yFromCanvas(y){
    return (250 - y)/50;
}
function rToCanvas(r){
    return (r/5) * 250;
}
function windowToCanvas(canvas, x, y){
    let bbox = canvas.getBoundingClientRect();
    return {x: x -bbox.left * (canvas.width / bbox.width),
        y: y - bbox.top * (canvas.height / bbox.height)
    };
}

function drawArea(r){
    ctx.clearRect(0,0, 500, 500);
    r = rToCanvas(r);

    ctx.beginPath();
    ctx.moveTo(250, 250);
    ctx.lineTo(250 - (r/2), 250);
    ctx.lineTo(250 - (r/2),  250 + r);
    ctx.lineTo(250, 250 + r);
    ctx.lineTo(250, 250);
    ctx.fill();

    ctx.beginPath();
    ctx.moveTo(250, 250);
    ctx.lineTo(250, 250 + (r/2));
    ctx.lineTo(250 + r, 250);
    ctx.lineTo(250, 250);
    ctx.fill();

    ctx.beginPath();
    ctx.arc(250, 250, r/2, 0, Math.PI/2, false);
    ctx.moveTo(250, 250 - (r/2));
    ctx.lineTo(250, 250);
    ctx.lineTo(250 + (r/2), 250);
    ctx.fill();

    drawAxes();
    drawDots();
}

function drawDots(){
    dots.forEach(addDot);
}

function addDot(dot){
    x = xToCanvas(dot.getX());
    y = yToCanvas(dot.getY());
    ctx.fillStyle = "black";
    ctx.fillRect(x, y, 3, 3);
    ctx.fillStyle = "deeppink";

}