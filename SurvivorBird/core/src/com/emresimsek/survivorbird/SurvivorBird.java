package com.emresimsek.survivorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.awt.Shape;
import java.util.Random;

public class SurvivorBird extends ApplicationAdapter {
	Texture background;
	Texture bird;
	Texture bee1;
	Texture bee2;
	Texture bee3;
	float birdX=0;
	float birdY=0;
	float velocity=0;
	float enemyVelocity=7;
	float distance=0;
	float gravity=0.9f;
	int gameState=0;//game Start
	int numberOfEnemies=4;
	int score=0;
	int scoreEnemy=0;
	float [] enemyX=new float[numberOfEnemies];
	float [] enemyOffSet1=new float[numberOfEnemies];
	float [] enemyOffSet2=new float[numberOfEnemies];
	float [] enemyOffSet3=new float[numberOfEnemies];
	Circle[] enemyCircle1;
	Circle[] enemyCircle2;
	Circle[] enemyCircle3;
	Circle birdCircle;
	Random random;
	SpriteBatch batch;
	BitmapFont font;
	BitmapFont font2;


	@Override
	public void create () {
		batch=new SpriteBatch();
		random=new Random();
		background=new Texture("background.png");
		bird=new Texture("bird.png");
		bee1=new Texture("bee.png");
		bee2=new Texture("bee.png");
		bee3=new Texture("bee.png");

		birdX=Gdx.graphics.getWidth()/4;
		birdY=Gdx.graphics.getHeight()/3;
		
		//Karakterlerin etrafına colider
		birdCircle=new Circle();
		enemyCircle1=new Circle[numberOfEnemies];
		enemyCircle2=new Circle[numberOfEnemies];
		enemyCircle3=new Circle[numberOfEnemies];

		//Score fontunun özellikleri
		font=new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(4);
		//Play Again fontunun özellikleri
		font2=new BitmapFont();
		font2.setColor(Color.WHITE);
		font2.getData().setScale(6);

		distance=Gdx.graphics.getWidth()/2;


		for (int i=0; i<numberOfEnemies;i++)
		{
			// Arıların  height değerlerine random sayı atma
			enemyOffSet1[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
			enemyOffSet2[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
			enemyOffSet3[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);

			enemyX[i]=Gdx.graphics.getWidth()-bee1.getWidth()/2+i*distance;

			enemyCircle1[i]=new Circle();
			enemyCircle2[i]=new Circle();
			enemyCircle3[i]=new Circle();

		}


	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		//oyun başladımı
		if (gameState==1)
		{
			//skor arttırma
			if (enemyX[scoreEnemy]<birdX)
			{
				score++;
				if (scoreEnemy<numberOfEnemies-1){
					scoreEnemy++;
				}
				else
				{
					scoreEnemy=0;
				}
			}
			//tıklandığında havaya kalksın
			if (Gdx.input.justTouched())
			{
				velocity=-15;
			}

			for (int i=0; i<numberOfEnemies;i++)
			{
				if (enemyX[i]<Gdx.graphics.getWidth()/15)
				{
					enemyX[i]=enemyX[i]+numberOfEnemies*distance;

					enemyOffSet1[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
					enemyOffSet2[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
					enemyOffSet3[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
				}
				else
				{
					enemyX[i]=enemyX[i]-enemyVelocity;
				}

				batch.draw(bee1,enemyX[i],Gdx.graphics.getHeight()/2+enemyOffSet1[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
				batch.draw(bee2,enemyX[i],Gdx.graphics.getHeight()/2+enemyOffSet2[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
				batch.draw(bee3,enemyX[i],Gdx.graphics.getHeight()/2+enemyOffSet3[i],Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);

				enemyCircle1[i]=new Circle(enemyX[i] + Gdx.graphics.getWidth()/30 , Gdx.graphics.getHeight()/2 + enemyOffSet1[i] + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
				enemyCircle2[i]=new Circle(enemyX[i] + Gdx.graphics.getWidth()/30 , Gdx.graphics.getHeight()/2 + enemyOffSet2[i] + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
				enemyCircle3[i]=new Circle(enemyX[i] + Gdx.graphics.getWidth()/30 , Gdx.graphics.getHeight()/2 + enemyOffSet3[i] + Gdx.graphics.getHeight()/20,Gdx.graphics.getWidth()/30);
			}

			//kuş oyun alanında
			if (birdY>0)
			{
				velocity=velocity+gravity;
				birdY=birdY-velocity;
			}
			//kuş yere düşerse
			else
			{
				gameState=2;
			}

		}
		// oyun tıklandığında başlat
		else if(gameState==0 )
		{
			if (Gdx.input.justTouched())
			{
				gameState=1;
			}
		}
		//oyun bitti
		else if (gameState==2)
		{
			font2.draw(batch,"Game Over! Tap To Play Again!",270,Gdx.graphics.getHeight()/2);

			//kuşun konumunu eski haline alma
			birdY=Gdx.graphics.getHeight()/3;
			for (int i=0; i<numberOfEnemies;i++)
			{
				enemyOffSet1[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
				enemyOffSet2[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);
				enemyOffSet3[i]=(random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-200);

				enemyX[i]=Gdx.graphics.getWidth()-bee1.getWidth()/2+i*distance;

				enemyCircle1[i]=new Circle();
				enemyCircle2[i]=new Circle();
				enemyCircle3[i]=new Circle();

			}
			velocity=0;
			scoreEnemy=0;
			score=0;

			if (Gdx.input.justTouched())
			{
				gameState=1;
			}

		}

		//Tasarımlar
		batch.draw(bird,birdX,birdY, Gdx.graphics.getWidth()/15,Gdx.graphics.getHeight()/10);
		font.draw(batch,String.valueOf(score),100,200);
		batch.end();
		birdCircle.set(birdX+Gdx.graphics.getWidth()/30,birdY+Gdx.graphics.getWidth()/30,Gdx.graphics.getWidth()/30);

		for (int i=0; i<numberOfEnemies;i++)
		{
			//Çarpışma algılamak
			if (Intersector.overlaps(birdCircle,enemyCircle1[i]) || Intersector.overlaps(birdCircle,enemyCircle2[i]) || Intersector.overlaps(birdCircle,enemyCircle3[i]))
			{
				gameState=2;
			}
		}

	}
	
	@Override
	public void dispose () {

	}
}
