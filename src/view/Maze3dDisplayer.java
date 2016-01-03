package view;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import algorithms.mazeGenerators.Maze3d;
import algorithms.mazeGenerators.MyMaze3dGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.mazeGenerators.PrimMaze3dGenerator;
import algorithms.search.Solution;

public class Maze3dDisplayer extends MazeDisplayer{


	Maze3d maze;
	int characterX;
	int characterY;
	int characterZ;
	int exitX;
	int exitY;
	int exitZ;
	ExecutorService threadSolve;
	Thread solve;
	Thread run;
	boolean running;
	 /**
     * DisplayMaze3D constructor
     */
	public Maze3dDisplayer(Composite parent, int style) {
		super(parent, style);
		threadSolve = Executors.newFixedThreadPool(1);

	}	
	/**
     * get the maze
     * @return maze-the maze
     */
	public Maze3d getMaze() {
		return maze;
	}
	/**
     * set the maze
     * @param maze-the maze
     */
	public void setMaze(Maze3d maze) {
		this.maze = maze;
		characterX = maze.getStartPosition().getX();
		characterY = maze.getStartPosition().getY();
		characterZ = maze.getStartPosition().getZ();
		
		exitX = maze.getGoalPosition().getX();
		exitY = maze.getGoalPosition().getY();
		exitZ = maze.getGoalPosition().getZ();
	}

     /**
      * draw the maze 3d and the character
      */
	public void draw(){
		Image image = new Image(getDisplay(), "resources/parket.jpg");
		Image charachter = new Image(getDisplay(), "resources/runne.png");
		Image endGame = new Image(getDisplay(), "resources/EndGame.jpg");
		Image up = new Image(getDisplay(), "resources/up.png");
		Image down = new Image(getDisplay(), "resources/downn.jpg");
		Image upAndDown = new Image(getDisplay(), "resources/upanddown.png");
		setBackground(new Color(null, 192, 192, 192));
		
		setBackgroundImage(image);
		
    	addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				   e.gc.setForeground(new Color(null,255,255,255));
				   e.gc.setBackground(new Color(null,255,255,255));

				   int width=getSize().x;
				   int height=getSize().y;

				   int w=width/maze.getMaze()[0][0].length;
				   int h=height/maze.getMaze()[0].length;
				   
				   if(characterX == exitX && characterZ == exitZ && characterY == exitY)
					   e.gc.drawImage(endGame, 0, 0, 280, 296, 0, 0, getSize().x, getSize().y);

				   else
					   for(int i=0;i<maze.getMaze()[0].length;i++)
						      for(int j=0;j<maze.getMaze()[0][0].length;j++){
						          int x=j*w;
						          int y=i*h;
						          if(maze.getMaze()[characterY][i][j] == 0)
						              e.gc.fillRectangle(x,y,w,h);  
						          if(characterY+1 < maze.getMaze().length)
						        	  if(maze.getMaze()[characterY][i][j] == 0 && maze.getMaze()[characterY+1][i][j] == 0)
						        		  e.gc.drawImage(up, 0, 0, 225,225 , x, y, w, h);
						          if(characterY-1>= 0)
						        	  	if(maze.getMaze()[characterY][i][j] == 0 && maze.getMaze()[characterY-1][i][j] == 0)
						        	  		e.gc.drawImage(down, 0, 0, 257,425 , x, y, w, h);
						          if(characterY +1< maze.getMaze().length && characterY-1 >= 0)
						        	  if(maze.getMaze()[characterY][i][j] == 0 && maze.getMaze()[characterY+1][i][j] == 0 && maze.getMaze()[characterY-1][i][j] == 0)
						        		  e.gc.drawImage(upAndDown, 0, 0, 225,225 , x, y, w, h);
						          if(j == characterX && i == characterZ){
						        	  e.gc.drawImage(charachter, 0, 0, 297,285 , x, y, w, h);
						          }
						      }		
			}
				  
		});
	}
	/**
	 * move the character
	 * @param x
	 * @param y
	 * @param z
	 */
	private void moveCharacter(int y,int z, int x){
		if(characterX == exitX && characterZ == exitZ && characterY == exitY)
      	  return;
		
		if(y>=0 && y < maze.getMaze().length && z>=0 && z<maze.getMaze()[0].length && x>=0 && x<maze.getMaze()[0][0].length && maze.getMaze()[y][z][x] == 0){
			characterX=x;
			characterY=y;
			characterZ=z;

			run=new Thread(new Runnable() {
				
				@Override
				public void run() {
					redraw();
				}
			});
			getDisplay().syncExec(run);
		
		}
	}

	@Override
	public void moveUp() {
		int x = characterX;
		int y = characterY+1;
		int z = characterZ;
		
		moveCharacter(y, z, x);
	}

	@Override
	public void moveDown() {
		int x = characterX;
		int y = characterY-1;
		int z = characterZ;
		
		moveCharacter(y, z, x);
	}

	@Override
	public void moveLeft() {
		int x = characterX-1;
		int y = characterY;
		int z = characterZ;
		
		moveCharacter(y, z, x);
	}

	@Override
	public void moveRight() {
		int x = characterX+1;
		int y = characterY;
		int z = characterZ;
		
		moveCharacter(y, z, x);
	}

	@Override
	public void moveForward() {
		int x = characterX;
		int y = characterY;
		int z = characterZ-1;
		
		moveCharacter(y, z, x);
	}

	@Override
	public void moveBack() {
		int x = characterX;
		int y = characterY;
		int z = characterZ+1;
		
		moveCharacter(y, z, x);
	}
	/**
	 * display the solution
	 * @param sol-the Solution for display
	 */
	public void displaySolution(Solution<Position> sol)
	{
		this.running = true;
		solve=new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(running)
				{
				String[] position=sol.toString().split("\n");
				int x,y,z;
				for(int i=position.length-1; running && i>=0; i--)
				{
					String[] numbers = position[i].split(" ");
					y=Integer.parseInt(numbers[0]);
					z=Integer.parseInt(numbers[1]);
					x=Integer.parseInt(numbers[2]);
					  moveCharacter(y,z,x);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {}
				}
				}
			}
		});
		threadSolve.execute(solve);
	}
	
	/**
	 * set the running
	 * @param running-the running
	 */
	public void setRunning(boolean running){ 
		
		if(!running)
		{
			this.running=running;
			if(run != null)
				while(run.isAlive());
			threadSolve.shutdown();
		}
	}
	/**
	 * set that new solve is occur
	 * @param running
	 */
	public void newSolve(boolean running){ 
		
		if(!running)
			this.running=running;
	}
	/**
	 * get the x of character
	 * @return characterX-the x
	 */
	public int getCharacterX() {
		return characterX;
	}
	/**
	 * get the y of character
	 * @return characterY-the y
	 */
	public int getCharacterY() {
		return characterY;
	}
	/**
	 * get the z of character
	 * @return characterZ-the z
	 */
	public int getCharacterZ() {
		return characterZ;
	}
	/**
	 * return if thread for solve is in progress
	 * @return boolean running
	 */
	public boolean isSolving() { return running; }
	


}