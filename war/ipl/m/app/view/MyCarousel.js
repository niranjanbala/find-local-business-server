/*
 * File: app/view/MyCarousel.js
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

Ext.define('MyApp.view.MyCarousel', {
    extend: 'Ext.carousel.Carousel',

    config: {
        id: 'main',
        ui: 'light',
        listeners: [
            {
                fn: 'onMainActiveItemChange',
                event: 'activeitemchange'
            }
        ]
    },

    onMainActiveItemChange: function(container, value, oldValue, options) {
        var totalItems=container.getItems().length-2;
        if(container.getActiveIndex()===totalItems){
            var formParamsIn="";
            var lineIndex=1;
            var items=container.getItems();
            Ext.each(items, function(i,index) {
                var item=items.get(index);
                console.log(item);
                if(index>0 && index<=totalItems && item.getValueString) {                
                    var line=item.getValueString();            
                    formParamsIn=formParamsIn+"param"+lineIndex+"="+line+"&";  
                    lineIndex=lineIndex+1;
                }
            });    
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
        }
    }

});