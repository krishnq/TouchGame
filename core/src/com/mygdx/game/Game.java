package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Game extends ApplicationAdapter implements InputProcessor,ApplicationListener {
	private SpriteBatch batch;
	private BitmapFont font;
	private String message = "Touch something already!";
	private int w,h; //SCREEN HEIGHT , WIDTH
	private ShapeRenderer renderer;


	private class circleEnem{
		int centerX;
		int centerY;
		int radius;
	}
	private class rectEnem{
		int center1X;
		int center1Y;
		int rect_width;
		int rect_height;
	}

	private class ENEM{
		circleEnem[] c_enem;
		rectEnem[] r_enem;
	}

	private float oldX,oldY,newX,newY; // OLD is CIRCLE CENTER NEW IS TOUCH LOCATION
	private float radius; //RADIUS OF CIRCLE
	private int touch;	//2 IS BEGIN,
	private float rate; //CIRCLE RADIUS INCREASE SPEED
	private int score;
	protected boolean state;
	private ENEM enem;

	@Override
	public void create() {

		Gdx.app.log("myinfo", "Level class started");
		batch = new SpriteBatch();
		font = new BitmapFont();
		enem = new ENEM();

		renderer = new ShapeRenderer();
		radius = 50;
		rate = (float) 2;
		score = 0;

		font.setColor(Color.BLUE);
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		Gdx.input.setInputProcessor(this);

		oldX=0;
		oldY=0;
		touch=2;

		targetY = 0;
		targetX = 0;
		/*
		for(int i = 0; i < 5; i++){
		touches.put(i, new TouchInfo());
	}
	*/
		state = false;
		setTarget();
		setEnem();

	}

	public void setEnem(){
		enem.c_enem = new circleEnem[1];
		int len = enem.c_enem.length;
		enem.c_enem[0] = new circleEnem();
		enem.c_enem[0].centerX = w/2;
		enem.c_enem[0].centerY = h/2;
		enem.c_enem[0].radius = 20;


		enem.r_enem = new rectEnem[1];
		int len1 = enem.r_enem.length;
		enem.r_enem[0] = new rectEnem();
		enem.r_enem[0].center1X = w/2+20;
		enem.r_enem[0].center1Y = h/2+20;
		enem.r_enem[0].rect_height = 20;
		enem.r_enem[0].rect_width = 20;
	}

	public void drawEnem(){
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(Color.RED);
		for(int i=0;i<enem.c_enem.length;i++){
			renderer.circle(enem.c_enem[i].centerX,enem.c_enem[i].centerY,enem.c_enem[i].radius);
		}
		for(int i=0;i<enem.r_enem.length;i++){
			renderer.rect(enem.r_enem[i].center1X,enem.r_enem[i].center1Y,enem.r_enem[i].rect_width,enem.r_enem[i].rect_height);
		}
		renderer.end();
	}

	public boolean checkEnem(){
		for(int i=0;i<enem.c_enem.length;i++){
			int temp1=enem.c_enem[i].radius;
			float temp = dist(enem.c_enem[i].centerX,enem.c_enem[i].centerY,oldX,oldY);
			if(temp < temp1+radius){
				return false;
			}
		}
		for(int i=0;i<enem.r_enem.length;i++){
			int temp1=enem.c_enem[i].radius;
			float temp = dist(enem.c_enem[i].centerX,enem.c_enem[i].centerY,oldX,oldY);
			if(temp < temp1+radius){
				return false;
			}
		}
		return true;
	}

	public void drawCircle(float x, float y){
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(Color.BLACK);
		renderer.circle(x,y,radius);
		renderer.setColor(Color.WHITE);
		renderer.circle(x,y,radius-20);
		renderer.end();
		radius=radius+rate;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		float temp = dist(newX,newY,oldX,oldY);
		switch (touch){
			case 0:
				break;
			case 1:

				if(temp <= radius+20  && temp >= radius-40){
					oldY=newY;
					oldX=newX;
					radius=50;
				}
				touch = 0;
				break;
			case 3:
				if(newX>=10 && newX<=w-10 && newY>=10 && newY<=(h*.2)+10){
					oldX = newX;
					oldY = newY;
					radius = 50;
					touch = 1;
				}else touch=2;
				break;
		}

		temp = dist(targetX,targetY,oldX,oldY);

		if(radius+30 >= temp){
			score++;
			setTarget();
			touch = 2;
			radius = 50;
			state = true;
			if(score%5 == 0) rate=rate+(float) 0.5;
		}

	/*if(!state)		*/drawTarget();
	drawEnem();

	//GAME SHOULD END HERE if the condition is ///////////////   TRUE    /////////////
	//if(!checkEnem()) score--;


		if(touch != 2){
			drawCircle(oldX, oldY);

		}else{
			renderer.begin(ShapeRenderer.ShapeType.Filled);
			renderer.setColor(Color.GRAY);
			renderer.rect(10,10,(float)(w-20),(float)(h*0.2));
			renderer.end();
		}



		batch.begin();
		message = "Score: " + Float.toString(score);
		float x = 2;
		float y = h-40;
		font.draw(batch, message, x, y);
		font.getData().setScale(3);
		batch.end();



	}

	int targetX,targetY;
	public void drawTarget(){
		renderer.begin(ShapeRenderer.ShapeType.Filled);
		renderer.setColor(Color.GREEN);
		renderer.circle(targetX,targetY,30);
		renderer.end();

	}
	public void setTarget(){
		float x=(float) Math.random();
		float y=(float) Math.random();
		if(x < .1) x=x+(float)0.1;
		if(x > .9) x=x-(float)0.1;
		if(y < .1) y=y+(float)0.1;
		if(y > .9) y=y-(float)0.1;
		targetX = (int) ((int)w*x);
		targetY = (int) (((int)h*y*.2)+(h*4/5));


	}

	public float dist(float a,float b,float c, float d){
		return (float) Math.sqrt(((a-c)*(a-c))+((b-d)*(b-d)));
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
	/*if(pointer < 5){
	touches.get(pointer).touchX = screenX;
	touches.get(pointer).touchY = screenY;
	touches.get(pointer).touched = true;
}
*/
		newX = screenX;
		newY = h-screenY;
		if(touch ==2 )touch = 3;
		else touch = 1;
		return true;
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
		renderer.dispose();
	}



	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

}

