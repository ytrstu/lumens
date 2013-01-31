package com.lumens.client.rpc.beans;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.smartgwt.client.widgets.drawing.DrawPath;
import com.smartgwt.client.widgets.drawing.DrawRect;
import com.smartgwt.client.widgets.drawing.Point;

/**
 *
 * @author shaofeng wang
 */
public class ClientElementLink extends DrawPath implements IsSerializable
{
    private ClientTransformElement outElement;
    private ClientTransformElement inElement;
    private transient DrawRect anchorPoint;
    private final static int anchorSize = 8;
    private final static int deltaLength = 12;
    private final static int deltaOffset = 2;

    public ClientElementLink()
    {
        this.setLineWidth(2);
        anchorPoint = new DrawRect();
        anchorPoint.setWidth(anchorSize);
        anchorPoint.setHeight(anchorSize);
        anchorPoint.setFillColor("#FF9900");
        anchorPoint.setLineWidth(1);
    }

    public DrawRect getAnchorPoint()
    {
        return anchorPoint;
    }

    public void set(ClientTransformElement in, ClientTransformElement out)
    {
        inElement = in;
        outElement = out;
        if (in != null)
            in.addOutLink(this);
        if (out != null)
            out.addInLink(this);
        if (in != null && out != null)
            updatePosition();
    }

    public void remove()
    {
        if (outElement != null)
            outElement.removeInLink(this);
        if (inElement != null)
            inElement.removeOutLink(this);
        anchorPoint.erase();
        erase();
    }

    public void updatePosition()
    {
        Point[] points = buildLinkPath();
        setPoints(points);
        anchorPoint.setCenter(points[0].getX(), points[0].getY());
    }

    public ClientTransformElement getIn()
    {
        return this.inElement;
    }

    public ClientTransformElement getOut()
    {
        return this.outElement;
    }

    private Point[] buildLinkPath()
    {
        Point[] pathPoints = buildNewPath();
        Point[] arrawPoints = buildTrianglePath(pathPoints);
        Point[] points = new Point[pathPoints.length + arrawPoints.length + 1];
        int i = 0, j = 0;
        while (i < pathPoints.length)
        {
            points[i] = pathPoints[i];
            ++i;
        }
        while (j < arrawPoints.length)
        {
            points[i] = arrawPoints[j];
            ++i;
            ++j;
        }
        points[i] = pathPoints[pathPoints.length - 1];

        return points;
    }

    private Point[] buildTrianglePath(Point[] points)
    {
        int i = (points.length == 2 ? 1 : 2);
        int j = (points.length == 2 ? 0 : 1);
        int s_x = points[i].getX(), s_y = points[i].getY(),
                e_x = points[j].getX(), e_y = points[j].getY();
        if (s_y == e_y)
        {
            if (s_x > e_x)
            {
                return new Point[]
                        {
                            new Point(s_x - deltaLength, s_y - deltaOffset),
                            new Point(s_x - deltaLength, s_y + deltaOffset)
                        };
            } else
            {
                return new Point[]
                        {
                            new Point(s_x + deltaLength, s_y - deltaOffset),
                            new Point(s_x + deltaLength, s_y + deltaOffset)
                        };
            }
        }
        if (s_y > e_y)
        {
            return new Point[]
                    {
                        new Point(s_x - deltaOffset, s_y - deltaLength),
                        new Point(s_x + deltaOffset, s_y - deltaLength)
                    };
        }

        return new Point[]
                {
                    new Point(s_x - deltaOffset, s_y + deltaLength),
                    new Point(s_x + deltaOffset, s_y + deltaLength)
                };
    }

    private Point[] buildNewPath()
    {
        int inLeft = inElement.getLeft();
        int inTop = inElement.getTop();
        int inWidth = inElement.getWidth();
        int inHeight = inElement.getHeight();
        int inRight = inElement.getRight();
        int inBottom = inElement.getBottom();

        int outLeft = outElement.getLeft();
        int outTop = outElement.getTop();
        int outWidth = outElement.getWidth();
        int outHeight = outElement.getHeight();
        int outRight = outElement.getRight();
        int outBottom = outElement.getBottom();

        int inCenterX = inLeft + inWidth / 2;
        int inCenterY = inTop + inHeight / 2;
        int outCenterX = outLeft + outWidth / 2;
        int outCenterY = outTop + outHeight / 2;

        if (inRight < outCenterX && inCenterY > (outBottom + deltaLength))
        {
            // in_right --> out_bottom
            return new Point[]
                    {
                        new Point(inRight, inCenterY),
                        new Point(outCenterX, inCenterY),
                        new Point(outCenterX, outBottom)
                    };
        } else if (inRight <= outCenterX && inCenterY <= (outBottom + deltaLength) && inCenterY >= (outTop - deltaLength))
        {
            // in_right --> out_left
            int y = (inCenterY + outCenterY) / 2;
            return new Point[]
                    {
                        new Point(inRight, y),
                        new Point(outLeft, y)
                    };
        } else if (inRight < outCenterX && inCenterY < outTop)
        {
            //in_right --> out_top
            return new Point[]
                    {
                        new Point(inRight, inCenterY),
                        new Point(outCenterX, inCenterY),
                        new Point(outCenterX, outTop)
                    };
        } else if (inBottom <= outTop && inRight >= outCenterX && inLeft <= outCenterX)
        {
            // in_bottom --> out_top
            int x = (inCenterX + outCenterX) / 2;
            return new Point[]
                    {
                        new Point(x, inBottom),
                        new Point(x, outTop)
                    };
        } else if (outCenterX < inLeft && outTop > (inCenterY + deltaLength))
        {
            // in_left --> out_top
            return new Point[]
                    {
                        new Point(inLeft, inCenterY),
                        new Point(outCenterX, inCenterY),
                        new Point(outCenterX, outTop)
                    };
        } else if (outRight <= inLeft && inCenterY <= (outBottom + deltaLength) && inCenterY >= (outTop - deltaLength))
        {
            // in_left --> out_right
            int y = (inCenterY + outCenterY) / 2;
            return new Point[]
                    {
                        new Point(inLeft, y),
                        new Point(outRight, y)
                    };
        } else if (outCenterX < inLeft && outTop < inCenterY)
        {
            // in_left --> out_bottom
            return new Point[]
                    {
                        new Point(inLeft, inCenterY),
                        new Point(outCenterX, inCenterY),
                        new Point(outCenterX, outBottom)
                    };
        } else if (inTop >= outBottom && outCenterX >= inLeft && outCenterX <= inRight)
        {
            // in_top --> out_bottom
            int x = (inCenterX + outCenterX) / 2;
            return new Point[]
                    {
                        new Point(x, inTop),
                        new Point(x, outBottom)
                    };
        }

        return buildPathPoint(
                inLeft, inTop, inWidth, inHeight,
                outLeft, outTop, outWidth, outHeight,
                inCenterX, inCenterY, outCenterX, outCenterY);
    }

    private Point[] buildPathPoint(
            int inLeft, int inTop, int inWidth, int inHeight,
            int outLeft, int outTop, int outWidth, int outHeight,
            int inCenterX, int inCenterY, int outCenterX, int outCenterY)
    {
        int[] min = new int[]
        {
            outCenterX, outTop, distance(inCenterX, inCenterY, outCenterX,
                                         outTop)
        };
        min = minDistance(inCenterX, inCenterY, min[0], min[1], min[2],
                          outLeft + outWidth,
                          outCenterY);
        min = minDistance(inCenterX, inCenterY, min[0], min[1], min[2],
                          outCenterX,
                          outTop + outHeight);
        min = minDistance(inCenterX, inCenterY, min[0], min[1], min[2], outLeft,
                          outCenterY);
        Point end = new Point(min[0], min[1]);

        min = new int[]
        {
            inCenterX, inTop, distance(outCenterX, outCenterY, inCenterX, inTop)
        };
        min = minDistance(end.getX(), end.getY(), min[0], min[1], min[2],
                          inLeft + inWidth,
                          inCenterY);
        min = minDistance(end.getX(), end.getY(), min[0], min[1], min[2],
                          inCenterX,
                          inTop + inHeight);
        min = minDistance(end.getX(), end.getY(), min[0], min[1], min[2], inLeft,
                          inCenterY);
        Point start = new Point(min[0], min[1]);

        return new Point[]
                {
                    start, end
                };
    }

    private int[] minDistance(int x, int y, int foundX, int foundY, int distance,
                              int edgeX,
                              int edgeY)
    {
        int distanceToEdge = distance(x, y, edgeX, edgeY);
        if (distanceToEdge < distance)
        {
            return new int[]
                    {
                        edgeX, edgeY, distanceToEdge
                    };
        }
        return new int[]
                {
                    foundX, foundY, distance
                };
    }

    private int distance(int x1, int y1, int x2, int y2)
    {
        return (int) (Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
