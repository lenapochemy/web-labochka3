class CanvasDrawer{

    canvas;
    ctx;
    lastR;
    constructor(canvas) {
        this.canvas = canvas;
        this.ctx = canvas.getContext("2d");
        this.ctx.fillStyle = "deeppink";
        this.drawAxes();
    }

    clickDot(event){
        let loc = this.windowToCanvas(canvas, event.clientX, event.clientY);
        let x = this.xFromCanvas(loc.x);
        let y = this.yFromCanvas(loc.y);
       // console.log("x: " + x + " y: " + y);
        if(this.lastR == null){
            alert('выберите радиус сначала');
        }

        addDotFromCanvas(
            [
                {name: "x", value: x.toString()},
                {name: "y", value: y.toString()},
                {name: "r", value: this.lastR.toString()}
            ]
        )
        updateCan();

    }

    drawDot(x, y, r, result){
        x = this.xToCanvas(x);
        y = this.yToCanvas(y);
        if(r === this.lastR) {
            if (result) this.ctx.fillStyle = "green";
            else this.ctx.fillStyle = "red";
            this.ctx.fillRect(x, y, 3, 3);
            this.ctx.fillStyle = "deeppink";
        }
    }

    drawAllDots(){
       // console.log("in draw all dots");
        //var data = [];
        $("#table tr").each(function (){
            //let rowLength = table.rows.length;
            let row = $(this);
            let x = parseFloat(row.find("td:eq(0)").text());
            let y = parseFloat(row.find("td:eq(1)").text());
            let r = parseFloat(row.find("td:eq(2)").text());
            let result = (row.find("td:eq(3)").text() === "Точка попала");

         //   console.log("x: " + x + " y: " + y + " res: " + result);
            canvasDrawer.drawDot(x, y, r, result);

    })
    }

    drawArea(r){
        this.lastR = r;
        this.ctx.clearRect(0,0, 500, 500);
        r = this.rToCanvas(r);

        this.ctx.beginPath();
        this.ctx.moveTo(250, 250);
        this.ctx.lineTo(250 - (r/2), 250);
        this.ctx.lineTo(250 - (r/2),  250 + r);
        this.ctx.lineTo(250, 250 + r);
        this.ctx.lineTo(250, 250);
        this.ctx.fill();

        this.ctx.beginPath();
        this.ctx.moveTo(250, 250);
        this.ctx.lineTo(250, 250 + (r/2));
        this.ctx.lineTo(250 + r, 250);
        this.ctx.lineTo(250, 250);
        this.ctx.fill();

        this.ctx.beginPath();
        this.ctx.arc(250, 250, r/2, 0, 3 * Math.PI/2, true);
        this.ctx.moveTo(250, 250 - (r/2));
        this.ctx.lineTo(250, 250);
        this.ctx.lineTo(250 + (r/2), 250);
        this.ctx.fill();

        this.drawAxes();
        this.drawAllDots();
        //drawDots();
    }

    drawAxes(){
        this.ctx.beginPath();
        this.ctx.moveTo(0, 250);
        this.ctx.lineTo(500, 250);
        this.ctx.moveTo(250, 0);
        this.ctx.lineTo(250, 500)
        this.ctx.stroke();
    }
    xToCanvas(x){
        return (x * 50) + 250;
    }
    yToCanvas(y){
        return 250 - (y * 50);
    }
    rToCanvas(r){
        return (r/5) * 250;
    }
    xFromCanvas(x){
        return (x - 250)/50;
    }
    yFromCanvas(y){
        return (250 - y)/50;
    }
    windowToCanvas(canvas, x, y){
        let bbox = canvas.getBoundingClientRect();
        return {x: x -bbox.left * (canvas.width / bbox.width),
            y: y - bbox.top * (canvas.height / bbox.height)
        };
    }
}