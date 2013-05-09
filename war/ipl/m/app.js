/*
 * File: app.js
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

Ext.Loader.setConfig({
    enabled: true
});

Ext.application({
    models: [
        'PointTableModel'
    ],
    stores: [
        'PointTableStore'
    ],
    views: [
        'MyCarousel',
        'MatchDisplay',
        'MatchInfoPanel',
        'Prediction'
    ],
    name: 'MyApp',
    startupImage: 'http://find-business.appspot.com/ipl/splash.png',

    launch: function() {
        Ext.Ajax.request({
            //first we give it the URL of the request. take not that this can only be local to the web server
            //you are on
            url: 'http://find-business.appspot.com/ipl/list?kind=Fixtures',

            //then we define a success method, which is called once the ajax request is successful
            success: function(response) {
                //the first argument returned is the reponse object, and that object has a property called
                //responseText which is the text of the file we just loaded. so we call setHtml which
                //will update the contentView with the text of the response
                //contentView.setHtml(response.responseText);
                var matches=Ext.decode(response.responseText);        
                var main=Ext.ComponentQuery.query('#main')[0];
                var matchDisplay = [];
                var matchLimit=5;       
                Ext.each(matches, function(match,index) {
                    if(index<matchLimit) {
                        var view=Ext.create('MyApp.view.MatchDisplay');
                        view.getMatchInfoPanel().team1=match.team1;  
                        view.getMatchInfoPanel().team2=match.team2;  
                        view.getMatchInfoPanel().updateView();
                        var teamOptions=[{
                            text: match.team1,
                            value: match.team1
                        }, {
                            text: match.team2,
                            value: match.team2
                        }];
                        var marginWinOptions=[{
                            text: '10 runs',
                            value: 10
                        }, {
                            text: '20 runs',
                            value: 20
                        }, {
                            text: '30 runs',
                            value: 30
                        }, {
                            text: '40 runs',
                            value: 40
                        }, {
                            text: '50 runs',
                            value: 50
                        }, {
                            text: '60 runs',
                            value: 60
                        }];
                        view.getWinnerSelectField().setOptions(teamOptions);                
                        view.getMarginWinSelectField().setOptions(marginWinOptions);
                        matchDisplay.push(view);
                    }
                });
                var chart=Ext.create('MyApp.view.Prediction');
                matchDisplay.push(chart);
                main.setItems(matchDisplay);
                main.setActiveItem(0);
            },
            failure: function() {
                //contentView.unmask();
            }
        });

        Ext.create('MyApp.view.MyCarousel', {fullscreen: true});
    }

});
