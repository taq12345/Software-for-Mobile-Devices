public class Main{

    public static void main(String []args){
       Guitar g = new Guitar(5,10);
       
       //INSTANTIATING INNER CLASS
       Guitar.Strings strings = g.new Strings();
       
       //INSTANTIATING NESTED CLASS
       Guitar.Material material = new Guitar.Material();
       
       //SETTING ATTRIBUTES
       g.setType("Stringed");
       g.setMake("Yamaha");
       g.setMode("Acoustic");
       
       //DISPLAYING ATTRIBUTES
       System.out.println("Make: " + g.getMake());
       System.out.println("Type: " + g.getType());
       System.out.println("Mode: " + g.getMode());
       System.out.println("Length: " + g.getLength());
       System.out.println("Weight: " + g.getWeight() + "\n");
       
       //CALLING INTERFACE FUNCTIONS AND CATCHING EXCEPTIONS
       try{
    	   g.strumDown();
       }
       catch(ArithmeticException e) {
    	   e.printStackTrace();
       }
       try{
    	   g.highTune();
       }
       catch(IndexOutOfBoundsException e) {
    	   e.printStackTrace();
       }
       try{
    	   g.GChord();
       }
       catch(NullPointerException e) {
    	   e.printStackTrace();
       }
       
       //CALLING INNER CLASS FUNCTION
       strings.setCompany("Daddario");
       System.out.println("\n" + "String company: " + strings.getCompany());
       
       //CALLING NESTED CLASS FUNCTION
       material.setName("Wood");
       System.out.println("\n" + "Material used: " + material.getName());
    }
}

abstract class Instrument{
   
   protected int length;
   protected int weight;
   protected String type;
   protected String make;
   static int count = 0;
   
   public abstract void setType(String type);
   public abstract void setMode(String string);
   public abstract String getType();
   public abstract String getMode();
   
   public final void setMake(String make)
   {
	   this.make = make;
   }
   public final String getMake()
   {
	   return this.make;
   }
   public void setLength(int length)
   {
       this.length = length;    
   }
   public void setWeight(int weight)
   {
       this.weight = weight;
   }
   public int getLength()
   {
       return this.length;
   }
   public int getWeight()
   {
       return this.weight;
   }
   
}

class Guitar extends Instrument implements Strummable, Tuneable{
   
    String mode;
    
    //INNER CLASS
	public class Strings {
		final int noOfStrings = 6;
		String company;
		double thickness;
		
		
		Strings()
		{
			System.out.println("Strings constructor called");
		}
		public void setCompany(String company) 
		{
			this.company = company;
		}
		public void setThickness(double thickness) 
		{
	         this.thickness = thickness;
	    }
		public String getCompany() 
		{
			return this.company;
		}
		public double getThickness()
		{
	        return this.thickness;
	    }
	}
	
	//STATIC INNER CLASS
	public static class Material
	{
		String name;
		
		Material(){
			System.out.println("Material constructor called\n");

		}
		public void setName(String name)
		{
			this.name = name;
		}
		public String getName()
		{
			return this.name;
		}
	}
	
	//CONSTRUCTOR
    Guitar(int length, int weight)
    {
        this.length = length;
        this.weight = weight;
        count++;
    
    }
    public void setType(String type)
    {
        this.type = type;
    }
    public void setMode(String mode)
    {
        this.mode = mode;
    }
	public String getType() {
		return this.type;
	}
	public String getMode() 
	{
		return this.mode;
	}
	public void strumDown() throws ArithmeticException
	{
		System.out.println("STRUMMING DOWN");
	}
	public void strumUp() 
	{
		System.out.println("STRUMMING UP");
	}
	public void GChord() throws NullPointerException
	{
		System.out.println("PLAYING G CHORD");
	}
	public void standardTune()
	{
		System.out.println("Tuned to standard tuning");
	}
	public void highTune() throws IndexOutOfBoundsException
	{
		System.out.println("Tuned to high tuning");
	}
	public void lowTune() 
	{
		System.out.println("Tuned to low tuning");
	}
	
}

interface Strummable{
	public void strumDown() throws ArithmeticException;
	public void strumUp();
	public void GChord() throws NullPointerException;
}

interface Tuneable {
	public void standardTune();
	public void highTune() throws IndexOutOfBoundsException;
	public void lowTune();
}

