package views;

import game.Asteroid;
import game.Projectile;
import game.Spaceship;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import tools.Polygon;
import tools.Vector;
import viewModel.ViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * An object of this class is responsible for drawing the current state
 * of the game over a JavaFX canvas, and handling GUI events.
 */
public class CanvasView {

  private final Canvas canvas;
  private final ViewModel viewModel;
  private GraphicsContext context;

  /**
   * @param canvas the canvas on which to draw the game
   * @param viewModel the viewModel to display and interact with
   */
  public CanvasView(Canvas canvas, ViewModel viewModel) {
    this.canvas = canvas;
    this.viewModel = viewModel;
    context = canvas.getGraphicsContext2D();
  }

   /**
   * Refresh the canvas, using the current state of the game.
   */
   public void render() {
    clear();
    renderBackground();
    render(viewModel.getAsteroids());
    render(viewModel.getSpaceship());
    renderScore(viewModel.getScore());
    renderMultiplier(viewModel.getScoreMultiplier());
    renderFuel(viewModel.getSpaceshipFuelPercentage());
    renderLives(viewModel.getSpaceship());
    renderProjectiles(viewModel.getProjectiles());
  }


  /**
   * Render all the visible asteroids.
   *
   * @param asteroids the list of visible asteroids
   */
  private void render(List<Asteroid> asteroids) {
    for (Asteroid asteroid : asteroids) {
      render(asteroid);
    }
  }


  /**
   * the font used to render the score.
   */
  private static final Font font =
    Font.font("DejaVu Sans", FontWeight.BOLD,48);

  /**
   * @param score the score to render
   */
  private void renderScore(double score) {
    context.setFill(Color.GREEN);
    context.setFont(font);
    String text = String.format("%d", Math.round(score));
    context.fillText(text, 50,50);
  }

  private void renderMultiplier(int multiplier) {
	context.setFill(Color.GREEN);
    context.setFont(font);
    String x = "X";
    String text = String.format("%d", Math.round(multiplier));
    context.fillText(x, 50,55);
    context.fillText(text, 50,60);
  }

  /**
   * Remove the current drawing from the canvas.
   */
  public void clear() {
    context.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
  }

  /**
   * Set the background image of the game.
   */
  public void renderBackground() {
    context.setFill(Color.BLACK);
    context.fillRect(0,0, Main.CANVAS_WIDTH, Main.CANVAS_HEIGHT);
  }


  /**
   * @param asteroid an asteroid to display
   */
  public void render(Asteroid asteroid) {
    context.setFill(Color.BROWN);
    render(asteroid.getShape());
  }
  
  private static final double OVAL_WIDTH = 4, OVAL_HEIGHT = 4;

  public void render(Projectile bullet) {
	context.setFill(Color.RED);
	context.fillOval(bullet.getPosition().getX(), bullet.getPosition().getY(), OVAL_WIDTH, OVAL_HEIGHT);
  }
  
  public void renderProjectiles(Set<Projectile> projectiles) {
	for (Projectile projectile : projectiles) {
		render(projectile);
	}
  }

  /**
   * @param shape a polygon to display
   */
  private void render(Polygon shape) {
    int nbPoints = shape.getVertices().size();
    double[] xs = new double[nbPoints];
    double[] ys = new double[nbPoints];
    int i = 0;
    for (Vector vertex : shape.getVertices()) {
      xs[i] = vertex.getX();
      ys[i] = vertex.getY();
      i++;
    }
    context.fillPolygon(xs, ys, nbPoints);
  }

  /**
   * @param spaceship a spaceship to display
   */
  public void render(Spaceship spaceship) {
    context.save();
    Vector position = spaceship.getPosition();
    context.translate(position.getX(), position.getY());
    context.rotate(spaceship.getDirectionAngle());
    renderSpaceShipImage(context, getImage(spaceshipImg));
    // if you want to add images to display over the ship, add them here
    // by calling again renderSpaceShipImage
    if(spaceship.isMainEngineOn()) renderSpaceShipImage(context, getImage(engineBurningImg));
    if(spaceship.isLeftEngineOn()) renderSpaceShipImage(context, getImage(clockwiseBurningImg));
    if(spaceship.isRightEngineOn()) renderSpaceShipImage(context, getImage(counterclockwiseBurningImg));
    if(spaceship.isReverseEngineOn()) renderSpaceShipImage(context, getImage(reverseBurningImg));
    if(spaceship.getInvunerableTime() > 0) context.setFill(Color.SILVER); // Change spaceship color when it is invulnerable
    context.restore();
  }


  /** renders an image of the spaceship. The context must be put in position
   * before calling this method, by rotation and translation, in such a
   * way that the spaceship would be in position (0,0) facing right.
   * @param context the drawing context rotated and translated
   * @param img the image to draw
   */
  private void renderSpaceShipImage(GraphicsContext context, Image img) {
    context.drawImage(
      img,
      - (double) PIXEL_SHIP_WIDTH / 2,
      - (double) PIXEL_SHIP_HEIGHT / 2,
      PIXEL_SHIP_WIDTH,
      PIXEL_SHIP_HEIGHT);
  }

  private void renderFuel(double fuelPercentage) {
	  context.fillRect(0, PIXEL_SCENE_HEIGHT, PIXEL_SCENE_WIDTH, PIXEL_SCENE_HEIGHT * fuelPercentage); // Need to be checked
	  context.setFill(Color.BLUE);
  }
  
  private void renderLives (Spaceship spaceship) {
	  context.save();
	  Vector position = new Vector(PIXEL_SCENE_WIDTH, 0);
	  for (int i=0; i<spaceship.getLife(); i++) {
		position = position.subtract(new Vector(PIXEL_SHIP_REDUCED_WIDTH, 0)); // Need to verify this position
		context.translate(position.getX(), 0);
		renderLiveImage(context,  getImage(spaceshipImg));
	  }
  }
  
  private void renderLiveImage (GraphicsContext context, Image img) {
	  context.drawImage(
	      img,
	      - (double) PIXEL_SHIP_REDUCED_WIDTH / 2,
	      - (double) PIXEL_SHIP_REDUCED_HEIGHT / 2,
	      PIXEL_SHIP_REDUCED_WIDTH,
	      PIXEL_SHIP_REDUCED_HEIGHT);
  }
  
  /**
   * Load an image from cache or from file.\
   *
   * @param path path to the file containing the image.
   * @return The image pointed to by the path.
   */
  private Image getImage(String path) {
    if (images.containsKey(path)) return images.get(path);
    Image image = new Image(getClass().getResource(path).toString());
    images.put(path, image);
    return image;
  }

  /* image cache */
  private static Map<String,Image> images = new HashMap<>();


  /* You can add more images here */
  private static final String spaceshipImg ="/resources/spaceship.png";
  private static final String engineBurningImg ="/resources/engine_burning.png";
  private static final String reverseBurningImg = "/resources/reverse_burning.png";
  private static final String clockwiseBurningImg =  "/resources/clockwise_burning.png";
  private static final String counterclockwiseBurningImg = "/resources/counterclockwise_burning.png";

  // dimensions of the ship image
  private static final int PIXEL_SHIP_WIDTH = 57;
  private static final int PIXEL_SHIP_HEIGHT = 46;
  
  private static final int REDUCE_IMAGE_COEFF = 5;
  private static final int PIXEL_SHIP_REDUCED_WIDTH = PIXEL_SHIP_WIDTH / REDUCE_IMAGE_COEFF;
  private static final int PIXEL_SHIP_REDUCED_HEIGHT = PIXEL_SHIP_HEIGHT / REDUCE_IMAGE_COEFF;

  // dimensions of powerup images
  private static final double PIXEL_POWERUP_WIDTH = 30;
  private static final double PIXEL_POWERUP_HEIGHT = 30;

  private static final double PIXEL_SCENE_WIDTH = 800;
  private static final double PIXEL_SCENE_HEIGHT = 800;

}
