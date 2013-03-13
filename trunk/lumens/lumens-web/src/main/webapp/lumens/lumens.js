/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 * Author     : shaofeng wang (shaofeng.cjpw@gmail.com)
*/
var Lumens = {
    version: 1.0
};

Lumens.create = function(parentId) {
    var lumensApp = {};
    var parent = $(parentId);

    // Initialize the header class component first
    var Header = {};
    Header.create = function(parentObj) {
        var parent = parentObj;
        parent.append('<div class="layout-header"/>');
        var headerDiv = parent.find('.layout-header');
        headerDiv.append('<div class="header-logo"><div class="header-log-text">LUMENS<div class="logo-bag"/></div></div>');
    }

    // Initialize the body class component
    var Accordian = {};
    Accordian.create = function(parentObj, titleText, accordionIdText, itemObjList) {
        var accordian = {};
        var parent = parentObj;
        var itemList = null;
        var title = titleText;
        var accordionId = accordionIdText;
        var itemPrefix=accordionIdText + '-item';
        var itemCount = itemObjList.length;

        accordian.itemCount = function() {
            return itemCount;
        }

        accordian.item = function(index) {
            return itemList.find('#'+itemPrefix+index);
        }

        // Build container
        parent.append('<div id="' + accordionId +'"><div class="accordian-item-title">' + title + '</div><ul style="margin: 0px; padding: 0px"/></div>');
        var accordianElement = $('#'+accordionId);
        accordianElement.addClass('accordian');
        var ul = accordianElement.find('ul');
        itemList = ul;
        // Build all item of the accordian
        for(var i = 0; i < itemObjList.length; ++i) {
            var itemHtml = '<li><div style="padding-top: 5px; position: absolute;">' +itemObjList[i] + '</div></li>'
            +'<li><div id="' + itemPrefix + i + '" style="width: 100%; height:100%"/></li>';
            ul.append(itemHtml);
        }
        // Hide all the content except the first
        var liodd = ul.find('li:odd');
        var lieven = ul.find('li:even');
        liodd.hide();

        // Add a padding to the first link
        var expand = ul.find('li:first-child');
        expand.animate( {
            paddingLeft:"30px"
        });
        expand.toggleClass('item-active');
        expand.next().stop().slideToggle(300);

        // Add the dimension class to all the content
        liodd.addClass('dimension');

        // Set the even links with an 'even' class
        ul.find('li:even:even').addClass('even');

        // Set the odd links with a 'odd' class
        ul.find('li:even:odd').addClass('even');

        // Show the correct cursor for the links
        lieven.css({
            'cursor':'pointer',
            'padding-left': '10px'
        });

        // Handle the click event
        lieven.click( function() {
            // Get the content that needs to be shown
            var cur_section = $(this);
            var cur = cur_section.next();

            // Get the content that needs to be hidden
            var old_section = ul.find('li.item-active');
            var old = old_section.next();

            // Make sure the content that needs to be shown
            // isn't already visible
            if ( cur_section.is('.item-active') )
                return;

            old_section.toggleClass('item-active');
            cur_section.toggleClass('item-active');

            // Hide the old content
            old.slideToggle(300);

            // Show the new content
            cur.stop().slideToggle(300);

            // Animate (add) the padding in the new link
            $(this).stop().animate( {
                paddingLeft:"30px"
            } );

            // Animate (remove) the padding in the old link
            old_section.stop().animate( {
                paddingLeft:"10px"
            } );
        } );

        return accordian;
    }

    var ComponentTree = {};
    ComponentTree.create = function(parentObj, strTreeIdText) {
        var componentTree = {};
        var parent = parentObj;
        var strTreeId = strTreeIdText;

        parent.append('<div id="'+strTreeId+'"/>');
        var dsTree = componentTree.dsTree = parent.find('#'+strTreeId);
        _canLog = false;
        dsTree.dynatree({
            onCreate: function(node) {
                if( !node.data.isFolder ) {
                    $(node.span).draggable({
                        appendTo: "#RightPane",
                        helper: "clone"
                    });
                }
            },
            persist: true,
            // TODO here Ajax request should be done !!!!!
            children: [ // Pass an array of nodes.
            {
                title: "Protocol",
                isFolder: true,
                icon: "../../../lumens/images/component/protocol.png",
                children: [
                {
                    title: "SOAP",
                    icon: "../../../lumens/images/component/soap.png",
                    addClass: "component-node"
                },

                {
                    title: "REST",
                    icon: "../../../lumens/images/component/rest.png",
                    addClass: "component-node"
                }
                ]
            },
            {
                title: "Application",
                isFolder: true,
                icon: "../../../lumens/images/component/apps.png",
                children: [
                {
                    title: "Database",
                    icon: "../../../lumens/images/component/database.png",
                    addClass: "component-node"
                }
                ]
            },
            {
                title: "File",
                isFolder: true,
                icon: "../../../lumens/images/component/file.png",
                children: [
                {
                    title: "XML",
                    icon: "../../../lumens/images/component/xml.png",
                    addClass: "component-node"
                },

                {
                    title: "Text",
                    icon: "../../../lumens/images/component/text.png",
                    addClass: "component-node"
                }
                ]
            }
            ],
            debugLevel:0
        });

        componentTree.loadData = function() {
        // TODO make the data loading as a json
        }

        return componentTree;
    }

    // Build the web header
    var header = Header.create(parent);
    // Initialize the splitter pane of the workspace
    $(parent).append('<div id="SplitterPane" class="layout-content splitter-pane-container"></div>');
    var splitterPane = $('#SplitterPane');
    splitterPane.append('<div id="LeftPane" style="position: absolute; z-index: 1; overflow-x: hidden; overflow-y: auto; left: 0px; width: 300px; height: 100%;"/>');
    splitterPane.append('<div id="RightPane" style="position: absolute; z-index: 1; width: 100%; height: 100%; overflow: hidden"/>');
    splitterPane.splitter({
        splitVertical: true,
        sizeLeft: true,
        accessKey: 'I'
    });
    // Load these settings from server ?
    var accordian = Accordian.create(splitterPane.find("#LeftPane"), "Toolbox", "toolbar",
        ["Datasource", "Processor", "Settings"]);
    // Build compononet tree UI
    var componentTree = ComponentTree.create(accordian.item(0), "componentTree");

    lumensApp.run = function() {
        // Load the mandory data from server first
        // Do some settings on the web page, such status and so on
        // Load the component tree by AJAX
        componentTree.loadData();
    }

    return lumensApp;
};

(function($) {
    // TODO
    // ******************************************************************************************
    // Nav header
    function Header(container) {
        this.container = container;
        this.create = function() {
            this.container.append('<div class="layout-header"/>');
            var header = $('.layout-header');
            header.append('<div class="header-logo"/>');
        };
    }

    function Accordian(parent, title, myId) {
        this.parent = parent;
        this.title = title;
        this.myId = myId;
        this.itemPrefix=myId + '-item';
        this.itemSize = 0;

        this.getItem = function(index) {
            return this.itemList.find('#'+this.itemPrefix+index);
        };

        this.itemCount = function() {
            return this.itemSize;
        };

        this.create = function(itemList) {
            this.itemSize = itemList.length;
            // Build container
            var accordianHtml =
            '<div id="' + myId +'">'
            +'<div class="accordian-item-title">' + title + '</div>'
            +'<ul style="margin: 0px; padding: 0px"/>'
            +'</div>';
            this.parent.append(accordianHtml);
            var accordian = $('#'+myId);
            accordian.addClass('accordian');
            var ul = accordian.find('ul');
            this.itemList = ul;
            // Build all item of the accordian
            for(var i = 0; i < itemList.length; ++i) {
                var itemHtml = '<li><div style="padding-top: 5px; position: absolute;">'
                +itemList[i] + '</div></li>'
                +'<li><div id="' + this.itemPrefix + i + '" style="width: 100%; height:100%"/></li>';
                ul.append(itemHtml);
            }
            // Hide all the content except the first
            var liodd = ul.find('li:odd');
            var lieven = ul.find('li:even');
            liodd.hide();

            // Add a padding to the first link
            var expand = ul.find('li:first-child');
            expand.animate( {
                paddingLeft:"30px"
            });
            expand.toggleClass('item-active');
            expand.next().stop().slideToggle(300);

            // Add the dimension class to all the content
            liodd.addClass('dimension');

            // Set the even links with an 'even' class
            ul.find('li:even:even').addClass('even');

            // Set the odd links with a 'odd' class
            ul.find('li:even:odd').addClass('even');

            // Show the correct cursor for the links
            lieven.css({
                'cursor':'pointer',
                'padding-left': '10px'
            });

            // Handle the click event
            lieven.click( function() {
                // Get the content that needs to be shown
                var cur_section = $(this);
                var cur = cur_section.next();

                // Get the content that needs to be hidden
                var old_section = ul.find('li.item-active');
                var old = old_section.next();

                // Make sure the content that needs to be shown
                // isn't already visible
                if ( cur_section.is('.item-active') )
                    return;

                old_section.toggleClass('item-active');
                cur_section.toggleClass('item-active');

                // Hide the old content
                old.slideToggle(300);

                // Show the new content
                cur.stop().slideToggle(300);

                // Animate (add) the padding in the new link
                $(this).stop().animate( {
                    paddingLeft:"30px"
                } );

                // Animate (remove) the padding in the old link
                old_section.stop().animate( {
                    paddingLeft:"10px"
                } );
            } );
        };
    }

    function ComponentTree(parent, strTreeId) {
        this.parent = parent;
        this.strTreeId = strTreeId;
        this.create = function() {
            this.parent.append('<div id="'+this.strTreeId+'"/>');
            var dsTree = this.parent.find('#'+this.strTreeId);
            _canLog = false;
            dsTree.dynatree({
                onCreate: function(node) {
                    if( !node.data.isFolder ) {
                        $(node.span).draggable({
                            appendTo: "#RightPane",
                            helper: "clone"
                        });
                    }
                },
                persist: true,
                // TODO here Ajax request should be done !!!!!
                children: [ // Pass an array of nodes.
                {
                    title: "Protocol",
                    isFolder: true,
                    icon: "../../../lumens/images/component/protocol.png",
                    children: [
                    {
                        title: "SOAP",
                        icon: "../../../lumens/images/component/soap.png",
                        addClass: "component-node"
                    },

                    {
                        title: "REST",
                        icon: "../../../lumens/images/component/rest.png",
                        addClass: "component-node"
                    }
                    ]
                },
                {
                    title: "Application",
                    isFolder: true,
                    icon: "../../../lumens/images/component/apps.png",
                    children: [
                    {
                        title: "Database",
                        icon: "../../../lumens/images/component/database.png",
                        addClass: "component-node"
                    }
                    ]
                },
                {
                    title: "File",
                    isFolder: true,
                    icon: "../../../lumens/images/component/file.png",
                    children: [
                    {
                        title: "XML",
                        icon: "../../../lumens/images/component/xml.png",
                        addClass: "component-node"
                    },

                    {
                        title: "Text",
                        icon: "../../../lumens/images/component/text.png",
                        addClass: "component-node"
                    }
                    ]
                }
                ],
                debugLevel:0
            });
        }
    }

    function ComponentBox(holder) {
        this.COMP_List = [];
        this.SVG = {};
        this.create = function(width, height) {
            holder.append('<div id="holderElement"/>');
            holderElement = holder.find("#holderElement");
            holderElement.attr({
                style: "height:" + height + ";width:" + width + ";overflow:auto"
            });
            SVGhodlerElement = d3.select("#holderElement");
            // Initailize the SVG object
            this.SVG = SVGhodlerElement.append("svg")
            .attr("width", holderElement.width()-20)
            .attr("height",  holderElement.height()-20);
        }
        this.getComponentList = function() {
            return this.COMP_List;
        }
        this.addComponent = function(name, label, x, y) {
            function Component(SVG, name) {
                width_constant = 140;
                height_title_constant = 25;
                height_body_constant = 32;
                height_constant = height_title_constant + height_body_constant;
                this.SVG = SVG;
                this.G = SVG.append('g');
                this.links = [];
                this.getSize = function() {
                    return {
                        width: width_constant,
                        height: height_constant
                    };
                }
                this.link = function(c) {
                    var s = this.getPosition();
                    var t = c.getPosition();
                    var size = this.getSize();
                    function build_line_info(s, t, size) {
                        var s_left = s.x;
                        var s_top = s.y;
                        var s_width = size.width;
                        var s_height = size.height;
                        var s_right = s.x + size.width;
                        var s_bottom = s.y + size.height;

                        var t_left = t.x;
                        var t_top = t.y;
                        var t_width = size.width;
                        var t_height = size.height;
                        var t_right = t.x + size.width;
                        var t_bottom = t.y + size.height;

                        var s_center_x = s.x + s_width / 2;
                        var s_center_y = s.y + s_height / 2;
                        var t_center_x = t_left + t_width / 2;
                        var t_center_y = t_top + t_height / 2;
                        var delta = 0;

                        if (s_right < t_center_x &&
                            s_center_y > (t_bottom + delta)) {
                            // s_right --> t_bottom
                            return [{
                                x: s_right,
                                y:s_center_y
                            },
                            {
                                x: t_center_x,
                                y: s_center_y
                            },
                            {
                                x: t_center_x,
                                y: t_bottom
                            }];
                        }
                        else if (s_right <= t_center_x &&
                            s_center_y <= (t_bottom + delta) &&
                            s_center_y >= (t_top - delta)) {
                            // s_right --> t_left
                            var ty = (s_center_y + t_center_y) / 2;
                            return [{
                                x: s_right,
                                y: ty
                            },
                            {
                                x: t_left,
                                y: ty
                            }];
                        }
                        else if (s_right < t_center_x &&
                            s_center_y < t_top) {
                            //s_right --> t_top
                            return  [{
                                x: s_right,
                                y:s_center_y
                            },
                            {
                                x: t_center_x,
                                y: s_center_y
                            },
                            {
                                x: t_center_x,
                                y: t_top
                            }];
                        } else if (s_bottom <= t_top &&
                            s_right >= t_center_x &&
                            s_left <= t_center_x) {
                            // s_bottom --> t_top
                            var tx = (s_center_x + t_center_x) / 2;
                            return [{
                                x: tx,
                                y: s_bottom
                            },
                            {
                                x: tx,
                                y: t_top
                            }];
                        } else if (t_center_x < s_left &&
                            t_top > (s_center_y + delta)) {
                            // s_left --> t_top
                            return [{
                                x: s_left,
                                y:s_center_y
                            },
                            {
                                x: t_center_x,
                                y: s_center_y
                            },
                            {
                                x: t_center_x,
                                y: t_top
                            }];
                        } else if (t_right <= s_left &&
                            s_center_y <= (t_bottom + delta) &&
                            s_center_y >= (t_top - delta)) {
                            // s_left --> out_right
                            ty = (s_center_y + t_center_y) / 2;
                            return [{
                                x: s_left,
                                y: ty
                            },
                            {
                                x: t_right,
                                y: ty
                            }];
                        } else if (t_center_x < s_left &&
                            t_top < s_center_y) {
                            // s_left --> t_bottom
                            return [{
                                x: s_left,
                                y:s_center_y
                            },
                            {
                                x: t_center_x,
                                y: s_center_y
                            },
                            {
                                x: t_center_x,
                                y: t_bottom
                            }];
                        } else if (s_top >= t_bottom &&
                            t_center_x >= s_left &&
                            t_center_x <= s_right) {
                            // s_top --> t_bottom
                            tx = (s_center_x + t_center_x) / 2;
                            return [{
                                x: tx,
                                y: s_top
                            },
                            {
                                x: tx,
                                y: t_bottom
                            }];
                        }
                        return [];
                    }
                    var link = {
                        source: this,
                        target: c,
                        G : this.SVG.append('g'),
                        L : {},
                        update: function () {
                            var s = this.source.getPosition();
                            var t = this.target.getPosition();
                            var size = this.source.getSize();
                            this.L.attr("d", line(build_line_info(s, t, size)))
                        }
                    };
                    this.links.push(link);
                    c.links.push(link);
                    var line = d3.svg.line()
                    .x(function(d) {
                        return d.x;
                    })
                    .y(function(d) {
                        return d.y;
                    })
                    .interpolate("linear");
                    link.L = link.G.append("svg:path");
                    link.L.attr("d", line(build_line_info(s, t, size)))
                    .style({
                        "stroke-width": .5,
                        "stroke": "rgb(170, 170, 170)",
                        "fill": "none"
                    });
                    return link;
                }
                this.getPosition = function() {
                    var data = this.G.datum();
                    return {
                        x: data.x,
                        y: data.y
                    };
                }
                this.create = function(cx, cy) {
                    function updateSVG(cx, cy) {
                        var width = Number(SVG.attr("width"));
                        var height = Number(SVG.attr("height"));
                        var v = cx + width_constant*2;
                        if(width < v)
                            SVG.attr("width", v);
                        v = cy + height_constant*2;
                        if(height < v)
                            SVG.attr("height", v);
                    }
                    // build a demo draggable rect
                    function dragmove(d) {
                        if( !draggable )
                            return;
                        d.x += d3.event.dx;
                        d.y += d3.event.dy;
                        updateSVG(d.x, d.y);
                        G.attr("transform","translate(" + d.x + "," + d.y + ")");
                        for(var i = 0; i < links.length; ++i)
                            links[i].update();
                    }
                    updateSVG(cx, cy);
                    var G = this.G;
                    var links = this.links;
                    G.datum(function() {
                        return this.dataset;
                    });
                    var draggable = false;
                    this.G.data([{
                        x: cx,
                        y: cy
                    }]).attr("transform", function(d) {
                        return "translate(" + d.x + "," + d.y + ")";
                    })
                    .call(d3.behavior.drag()
                        .on("dragstart", function(d) {
                            var y = d3.event.sourceEvent.layerY;
                            if(y > (d.y+height_title_constant))
                                draggable = false;
                            else
                                draggable = true;
                        }).on("drag", dragmove));
                    // Body
                    this.G.append('rect').attr({
                        "height": height_body_constant,
                        "width": width_constant,
                        "y": function(d) {
                            return d.y + height_title_constant - cy;
                        }
                    })
                    .style({
                        "fill" : "rgb(236, 236, 236)",
                        "stroke": "rgb(177, 177, 177)",
                        "stroke-width": .3
                    });
                    // Title
                    this.G.append('rect').attr({
                        "height": height_title_constant,
                        "width": width_constant,
                        "cursor": "move",
                        "y": function(d) {
                            return d.y - cy;
                        }
                    })
                    .style({
                        "fill" : "rgb(214, 214, 214)",
                        "stroke": "rgb(177, 177, 177)",
                        "stroke-width": .5
                    });
                    this.G.append("image")
                    .attr({
                        "xlink:href": "lumens/images/status/16/disconnect.png",
                        "x": 8,
                        "y": 4,
                        "width": 16,
                        "height": 16
                    });
                    this.G.append("image")
                    .attr({// TODO here need to refine to load the image dynamicly
                        "xlink:href": "lumens/images/component/" + name.toLowerCase() +".png",
                        "x": 4,
                        "y": 28,
                        "width": 24,
                        "height": 24
                    });
                    this.G.append("text")
                    .attr({
                        "x": 36,
                        "y": 18,
                        "cursor": "move"
                    })
                    .text(name)
                    .style("font-size", "14px");
                    this.G.append("text")
                    .attr({
                        "x": 40,
                        "y": 46,
                        "cursor" : "default"
                    }).text(label)
                    .style("font-size", "12px")
                    .style("overflow", "hidden"); // TODO if the label is a long text, need to trim it as a correct length
                }
            }
            var COMP = new Component(this.SVG, name, label);
            COMP.create(x, y)
            this.COMP_List.push(COMP);
            return COMP;
        }
    }

    // Build lumens application layout
    $.fn.runLumensApplication = function() {
        var navHeader = new Header($(this));
        navHeader.create();

        // Build spiltter pane
        $(this).append('<div id="SplitterPane" class="layout-content splitter-pane-container"></div>');
        var splitterPane = $('#SplitterPane');
        splitterPane.append('<div id="LeftPane" style="position: absolute; z-index: 1; overflow-x: hidden; overflow-y: auto; left: 0px; width: 300px; height: 100%;"/>');
        splitterPane.append('<div id="RightPane" style="position: absolute; z-index: 1; width: 100%; height: 100%; overflow: hidden"/>');
        splitterPane.splitter({
            splitVertical: true,
            sizeLeft: true,
            accessKey: 'I'
        });

        // Build Left pane and component tree
        var leftPane = $('#LeftPane');
        var accordianPane = new Accordian(leftPane, 'Toolbox', 'Accordian-1');
        accordianPane.create(["Datasource", "Processor", "Settings"]);
        var count = accordianPane.itemCount();
        for(var i = 1; i < count; ++i) {
            var item = accordianPane.getItem(i);
            item.text('demo text ' + i);
        }
        var dataSourcePane = accordianPane.getItem(0);

        // Build tree
        var componentTree = new ComponentTree(dataSourcePane, "componentTree");
        componentTree.create();
        var rightPane = $('#RightPane');
        // Initailize the SVG object
        var box = new ComponentBox(rightPane);
        box.create("100%", "100%");
        // Build a demo draggable rect object
        rightPane.droppable({
            drop: function( event, ui ) {
                var paneOffset = $(this).offset();
                var offset = ui.helper.offset();
                box.addComponent(ui.helper.find('a').html(), "Untitled 1",
                    offset.left - paneOffset.left, offset.top - paneOffset.top);
            }
        });

        // TODO need to add the transform processor drag and drop operations

        // Add three demo component here
        var c1 = box.addComponent("SOAP", "source", 100, 100);
        var c2 = box.addComponent("Transform", "transform", 400, 200);
        var c3 = box.addComponent("Database", "target", 700, 100);
        var p = c1.getPosition();
        var link = c1.link(c2).target.link(c3);
    };
})(jQuery);