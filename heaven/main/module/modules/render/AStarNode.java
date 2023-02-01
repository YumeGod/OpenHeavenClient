/*
 * Decompiled with CFR 0.152.
 */
package heaven.main.module.modules.render;

public class AStarNode {
    private double x;
    private double z;
    private double heuristic;
    private AStarNode parent;

    public AStarNode(double x, double z) {
        this.x = x;
        this.z = z;
    }

    public AStarNode getParent() {
        return this.parent;
    }

    public void setParent(AStarNode parent) {
        this.parent = parent;
    }

    public double getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(int y) {
        this.z = y;
    }

    public double getHeuristic() {
        return this.heuristic;
    }

    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }
}

