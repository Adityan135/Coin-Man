package com.aditya.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;


import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int manstate=0;
	int pause=0;
	float gravity=0.2f;
	float velocity=0;
	int manY=0;
	int score=0;
	BitmapFont bitmapFont;
	Random random;
	ArrayList<Integer> coinXs=new ArrayList<>();
	ArrayList<Integer> coinYs=new ArrayList<>();
	ArrayList<Rectangle> coinRectangle=new ArrayList<>();
	ArrayList<Integer> bombXs=new ArrayList<>();
	ArrayList<Integer> bombYs=new ArrayList<>();
	ArrayList<Rectangle> bombRectangle=new ArrayList<>();
	Rectangle characterrect;
	Texture bomb;
	Texture coin;
	int coincount;
	int bombcount;
	int gamestate=0;
	Texture dizzy;
	Music sound;
	@Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
		man=new Texture[4];
		man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-2.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
        manY=Gdx.graphics.getHeight()/2;
        coin=new Texture("coin.png");
        bomb=new Texture("bomb.png");
        random=new Random();
        characterrect=new Rectangle();
        bitmapFont=new BitmapFont();
        bitmapFont.setColor(Color.WHITE);
        bitmapFont.getData().setScale(10);
        dizzy=new Texture("dizzy-1.png");
        sound=Gdx.audio.newMusic(Gdx.files.internal("bgm.ogg"));
        sound.setLooping(true);
        sound.setVolume(0.5f);
        sound.play();
	}
	public void makeCoin(){
		float height=random.nextFloat()*Gdx.graphics.getHeight();
		coinYs.add((int)height);
		coinXs.add(Gdx.graphics.getWidth());
	}
    public void makeBomb(){
		float height=random.nextFloat()*Gdx.graphics.getHeight();
		bombYs.add((int)height);
		bombXs.add(Gdx.graphics.getWidth());
	}
	@Override
	public void render () {

       batch.begin();
       batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
       if(gamestate==1){
		   if (bombcount<250){
			   bombcount++;
		   }
		   else {
			   bombcount=0;
			   makeBomb();
		   }
		   bombRectangle.clear();
		   for (int i=0;i<bombXs.size();i++){
			   batch.draw(bomb, bombXs.get(i), bombYs.get(i));
			   bombXs.set(i, bombXs.get(i) - 4);
			   bombRectangle.add(new Rectangle(bombXs.get(i),bombYs.get(i),bomb.getWidth(),bomb.getHeight()));
		   }
		   if(coincount<100){
			   coincount++;
		   }
		   else {
			   coincount=0;
			   makeCoin();
		   }
		   coinRectangle.clear();
		   for (int i=0;i<coinXs.size();i++) {
			   batch.draw(coin, coinXs.get(i), coinYs.get(i));
			   coinXs.set(i, coinXs.get(i) - 4);
			   coinRectangle.add(new Rectangle(coinXs.get(i),coinYs.get(i),coin.getWidth(),coin.getHeight()));
		   }

		   if(Gdx.input.justTouched()){
			   velocity=-10;
		   }
		   if(pause<8){
			   pause++;}
		   else {
			   pause=0;
			   if (manstate < 3) {
				   manstate++;
			   } else {
				   manstate = 0;
			   }
		   }
		   velocity=velocity+gravity;
		   manY-=velocity;
		   if(manY<=0){
			   manY=0;
		   }
	   }
       else if(gamestate==0){
       	if(Gdx.input.justTouched()){
       		gamestate=1;
		}
	   }
       else if(gamestate==2){
		   if(Gdx.input.justTouched()){
			   gamestate=1;
			   manY=Gdx.graphics.getHeight()/2;
			   score=0;
			   velocity=0;
			   coinXs.clear();
			   coinYs.clear();
			   coinRectangle.clear();
			   coincount=0;
			   bombXs.clear();
			   bombYs.clear();
			   bombRectangle.clear();
			   bombcount=0;

		   }
	   }
        if(gamestate==2){
        	batch.draw(dizzy,Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY);
		}
        else{
       batch.draw(man[manstate],Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY);}
       characterrect=new Rectangle(Gdx.graphics.getWidth()/2-man[manstate].getWidth()/2,manY,man[manstate].getWidth(),man[manstate].getHeight());
       for(int i=0;i<coinRectangle.size();i++){
       	if(Intersector.overlaps(characterrect,coinRectangle.get(i))){
         score++;
         coinRectangle.remove(i);
         coinXs.remove(i);
         coinYs.remove(i);
         break;
		}
	   }
		for(int i=0;i<bombRectangle.size();i++){
			if(Intersector.overlaps(characterrect,bombRectangle.get(i))){
               gamestate=2;
			}
		}
		bitmapFont.draw(batch,String.valueOf(score),100,200);
       batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		sound.dispose();
	}
}
