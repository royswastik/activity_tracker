function getData(blockName, districtName){
    var data = window.WebViewHandler.getData();
    console.log(data);
    return JSON.parse(data);
}

function getLayoutConfig(){
    var config = {
            title: 'Data Visualization',
            autosize: true,

            margin: {
                l: 0,
                r: 0,
                b: 0,
                t: 65
            },
            scene: {
                xaxis: { title: 'Acc X (m/s^2)' },
                yaxis: { title: 'Acc Y (m/s^2)' },
                zaxis: { title: 'Acc Z (m/s^2)' },
            },
            showlegend: true,
           legend: {"orientation": "h"}
        };
        return config;
}

function plotGraph(run, walk, jump, runDisplay, walkDisplay, jumpDisplay) {
    var trainingData = []
    if (runDisplay)
        trainingData.push({ activity: run, color: 'blue', name: "RUN" });
    if (walkDisplay)
        trainingData.push({ activity: walk, color: 'green', name: "WALK" })
    if (jumpDisplay)
        trainingData.push({ activity: jump, color: 'red', name: "JUMP" })

    var allTraces = [];
    for (var a = 0; a < trainingData.length; a++) {
        var currentColor = trainingData[a].color;
        var currentActivity = trainingData[a].activity;
        var currentActivityName = trainingData[a].name;

        for (var r = 0; r < currentActivity.length; r++) {
            var recordX = []
            var recordY = []
            var recordZ = []
            var record = currentActivity[r];
            for (var p = 0; p < record.length; p++) {
                if (p % 3 == 0) {
                    recordX.push(record[p]);
                } else if (p % 3 == 1) {
                    recordY.push(record[p]);
                }
                else if (p % 3 == 2) {
                    recordZ.push(record[p])
                }
            }
           var hideLegend = true;
            if (r == 0)
                hideLegend = false;
            var currentTrace = {
                x: recordX,
                y: recordY,
                z: recordZ,
                mode: 'lines',
                marker: {
                    color: currentColor,
                    size: 12,
                    symbol: 'circle',
                    line: {
                        color: 'rgb(0,0,0)',
                        width: 0
                    }
                },
                line: {
                    color: currentColor,
                    width: 1
                },
                type: 'scatter3d',
                name: currentActivityName,
                showlegend: !hideLegend
            };
            allTraces.push(currentTrace);
        }
    }
    Plotly.newPlot('container', allTraces, getLayoutConfig(), {displayModeBar: false});
}

