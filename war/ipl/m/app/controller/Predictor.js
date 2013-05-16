/*
 * File: app/controller/Predictor.js
 *
 * This file was generated by Sencha Architect version 2.1.0.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Sencha Touch 2.1.x library, under independent license.
 * License of Sencha Architect does not include license for Sencha Touch 2.1.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

Ext.define('MyApp.controller.Predictor', {
    extend: 'Ext.Base',

    statics: {
        analyze: function(container) {
            var clientSide=false;
            if(MyApp.app.matches.length<=15) {     
                clientSide=true;
            }
            var totalItems=container.getItems().length-2;
            //container.getActiveItem().getPreviousButton().setDisabled(false);
            if(container.getActiveIndex()===0) {   
                //container.getActiveItem().getPreviousButton().setDisabled(true);
            }
            if(container.getActiveIndex()===totalItems){
                var formParamsIn="";
                var lineIndex=1;
                var items=container.getItems();
                Ext.each(items, function(i,index) {
                    var item=items.get(index);
                    if(index>0 && index<=totalItems && item.getValueString) {                
                        var line=item.getValueString();            
                        formParamsIn=formParamsIn+"param"+lineIndex+"="+line+"&";  
                        lineIndex=lineIndex+1;
                    }
                });    
                if(clientSide) {
                    MyApp.controller.Predictor.analyzeLocal(items);
                }
                //else {
                var store=Ext.create("MyApp.store.PointTableStore",{autoLoad:false});
                store.setProxy({
                    type: 'rest',
                    api: {
                        read: "http://find-business.appspot.com/ipl/predict"
                    },
                    actionMethods: {
                        read: 'POST'
                    },
                    reader: {type:'json'},
                    writer: {type:'json'}
                });
                store.on('load', function(v,r,success) {        
                    items.get(totalItems+1).unmask(); 
                    if(success) {        
                        items.get(totalItems+1).loadData(r);                
                    }            
                });
                items.get(totalItems+1).mask(); 
                store.load({params: formParamsIn});
                //}
            }
        },

        analyzeLocal: function(userPredictions) {
            var ptMap=[];
            var matchMap=[];
            var matchRemainingMap=[];

            Ext.each(MyApp.app.pointTable, function(i,index) {
                var p=MyApp.app.pointTable[index];
                ptMap[p.teamName]=MyApp.controller.Predictor.createPointTableEntry(p);
            });
            Ext.each(MyApp.app.matches, function(i,index) {
                var m=MyApp.app.matches[index];
                matchMap[m.team1+"_"+m.team2]=MyApp.controller.Predictor.createMatchEntry(m);
            });
            var totalItems=userPredictions.length-2;
            var winningRuns=170;
            var overs=20.0;    
            Ext.each(userPredictions, function(i,index) {
                var item=userPredictions.get(index);
                if(index>0 && index<=totalItems && item.getValueString) {                        
                    var result=item.getValueString().split(",");
                    var key=result[0]+"_"+result[1];
                    var winner=result[2];
                    var loser=winner===result[0]?result[1]:result[0];        
                    var marginWin=Ext.Number.from(result[3]);
                    ptMap[winner].points=ptMap[winner].points+2;
                    ptMap[winner].forRuns=ptMap[winner].forRuns+winningRuns;
                    ptMap[winner].forOver=ptMap[winner].forOver+overs;

                    ptMap[winner].againstRuns=ptMap[winner].againstRuns+winningRuns-marginWin;
                    ptMap[winner].againstOver=ptMap[winner].againstOver+overs;
                    ptMap[winner].netrr=MyApp.controller.Predictor.computeRunRate(ptMap[winner]);
                    ptMap[loser].points=ptMap[loser].points+0;
                    ptMap[loser].forRuns=ptMap[winner].forRuns+winningRuns-marginWin;
                    ptMap[loser].forOver=ptMap[winner].forOver+overs;

                    ptMap[loser].againstRuns=ptMap[winner].againstRuns+winningRuns;
                    ptMap[loser].againstOver=ptMap[winner].againstOver+overs;
                    ptMap[loser].netrr=MyApp.controller.Predictor.computeRunRate(ptMap[loser]);
                    matchMap[key].winner=winner;
                    matchMap[key].margin=marginWin;
                }
            }); 
            var ii=0;
            Ext.each(MyApp.app.matches, function(i,index) {
                var m=MyApp.app.matches[index];
                if(matchMap[m.team1+"_"+m.team2].winner==="") {
                    matchRemainingMap[ii++]=MyApp.controller.Predictor.createMatchEntry(m);
                }
            });
            console.log(matchRemainingMap);      
        },

        createPointTableEntry: function(p) {
            return {
                points: Ext.Number.from(p.points),
                forRuns:Ext.Number.from(p.forRuns), 
                forOver: Ext.Number.from(p.forOver),
                againstOver: Ext.Number.from(p.againstOver),
                againstRuns: Ext.Number.from(p.againstRuns),
                netrr: Ext.Number.from(p.netrr)
            };
        },

        createMatchEntry: function(m) {
            return {
                team1: m.team1,
                team2:m.team2,
                winner:"",
                margin:0
            };
        },

        computeRunRate: function(p) {
            var  forAvg = p.forRuns / MyApp.controller.Predictor.getBallsInOver(p.forOver) * 6;
            var aAvg = p.againstRuns / MyApp.controller.Predictor.getBallsInOver(p.againstOver) * 6;
            return forAvg - aAvg;
        },

        getBallsInOver: function(overs) {
            var  numberOfBalls=Math.floor(overs)*6;
            numberOfBalls+=(overs-Math.floor(overs))*10;
            return numberOfBalls;
        }
    },

    config: {
    }

});