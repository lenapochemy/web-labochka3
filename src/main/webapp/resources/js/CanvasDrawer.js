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

    // обрабатывает клик по графику
    clickDot(event){
        console.log("aaaaa try draw");
        let loc = this.windowToCanvas(canvas, event.clientX, event.clientY);
        if(this.lastR == null){
            alert('выберите радиус сначала');
        }

        // получаем значения x и y
        let x = this.xFromCanvas(loc.x);
        let y = this.yFromCanvas(loc.y);
        console.log("x: " + x + " y: " + y);

        // сохраняем значение радиуса в сессии
        sessionStorage.setItem("lastR", this.lastR);

        //вызываем ремут комманд, передаем данные для точки
        addDotFromCanvas(
            [
                {name: "x", value: x.toString()},
                {name: "y", value: y.toString()},
                {name: "r", value: this.lastR.toString()}
            ]
        )
        console.log("оправили точку");

        checkUpdate(); //загружаем первую страницу пагинации
        updateButtons(); //обновляем кнопки пагинации

        //this.drawArea(this.lastR);
         updateCan();  //обновляем рисунок


    }


    //рисует точки на граыике
    drawDozt(x, y, r, result){
        console.log("зашли в рисование точки r=" + this.lastR);
        x = this.xToCanvas(x);
        y = this.yToCanvas(y);
        //if(r === this.lastR) {
            console.log("in if");
            if (result) this.ctx.fillStyle = "green";
            else this.ctx.fillStyle = "red";
           // this.ctx.arc(x, y, 1.5, 0, Math.PI * 2, false);
            this.ctx.fillRect(x, y, 3, 3);
            this.ctx.fillStyle = "deeppink";
       // }
        console.log("nbgj в рисование точки");
    }

    drawDot(x, y, r, result){
        console.log("r=" + r + " lastR=" + this.lastR);
        x = this.xToCanvas(x);
        y = this.yToCanvas(y);
        console.log("x=" + x + " y=" + y);
       if(r == this.lastR){
            console.log("in if");
            if(result) {this.ctx.fillStyle = "green";}
                else {this.ctx.fillStyle = "red";}
                this.ctx.fillRect(x, y, 3, 3);
                this.ctx.fillStyle = "deeppink";
        }
    }


    //рисует все точки из таблицы на график
    drawAllDots(){
       // console.log("in draw all dots");
        $("#table tr").each(function (){
            //let rowLength = table.rows.length;
            let row = $(this);
            let x = parseFloat(row.find("td:eq(1)").text());
            let y = parseFloat(row.find("td:eq(2)").text());
            let r = parseFloat(row.find("td:eq(3)").text());
            let result = (row.find("td:eq(4)").text() === "Точка попала");

            console.log("x: " + x + " y: " + y +" r: " + r +  " res: " + result);
            canvasDrawer.drawDot(x, y, r, result);

    })
    }


    //перерисовывает график - рисует область, оси и все точки
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
        console.log("обираемся рисовать точ4и");
        this.drawAllDots();
    }

    //рисует оси
    drawAxes(){
        this.ctx.beginPath();
        this.ctx.moveTo(0, 250);
        this.ctx.lineTo(500, 250);
        this.ctx.moveTo(250, 0);
        this.ctx.lineTo(250, 500)
        this.ctx.stroke();
    }

    //переводят координаты туда и обратно
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