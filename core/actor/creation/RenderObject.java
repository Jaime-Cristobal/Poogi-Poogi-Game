package com.mygdx.core.actor.creation;

public interface RenderObject
{
    void update(Object playerID);

    void display(float delta);

    void setDisplay(boolean val);

    boolean ifDisplayed();

    void respawn(float x, float y);

    void despawn();

    boolean ifCollide(Object PlayerID);

    void setTag(int id);

    int getTag();

    void setContainPortal(boolean val);

    boolean isContainPortal();

    CreateActor getFixture();
}
