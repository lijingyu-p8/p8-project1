# 设计模式

## 一、单例模式

### 1.初级模式ver_1

- 操作流程：定义private 无参构造函数，防止外部可以进行实例化，内部定义静态变量，进行实例化类，可以在类加载的时候进行实例化，由虚拟机进行单例控制，一个类只会加载一次。简单方便


- 缺点：未使用类时，就已经进行了初始化，但是简单易用，还是不错的


- 代码：


```
public class Singleton01 {
	private static Singleton01 singleton01 = new Singleton01();

	private Singleton01() {
	}

	public static Singleton01 getInstance() {
		return singleton01;
	}
}
```

### 2.初级模式ver_2

- 操作流程：定义静态内部类，和模式v1类似，可以解决v1的缺点,静态内部类不会随着外部类一起进行加载，只会在外部类调用内部类的时候进行加载。推荐使用


- 代码：

  ```
  public class Singleton02 {
  	private static class Singleton02Factory {
  		private final static Singleton02 singleton02 = new Singleton02();
  	}
  
  	public static Singleton02 getInstance() {
  		return Singleton02Factory.singleton02;
  	}
  }
  ```

### 3.中级模式

- 操作流程：执行同步代码块，进行双重检查，防止A线程第一次判断实例为空，此时B线程进行访问，拿到锁，进行实例化，释放锁，最后A线程又拿到锁，重复进行实例化的问题。加volatile 可以防止指令重排。


- 代码：

  ```
  public class Singleton03 {
  	private static volatile Singleton03 singleton03;
  
  	private Singleton03() {
  	}
  
  	public static Singleton03 getInstance() {
  		if (singleton03 == null) {
  			synchronized (Singleton03.class) {
  				// **双重判断
  				if (singleton03 == null) {
  					singleton03 = new Singleton03();
  				}
  			}
  		}
  		return singleton03;
  	}
  }
  ```

### 4.高级模式

- 操作流程：定义枚举类型，只定义一个INSTANCE枚举项。jvm会控制单例，也可以访问枚举内部的方法。枚举因为没有构造方法，可以防止反序列化。


- 代码：

  ```
  public enum Singleton04 {
  	INSTANCE;
  
  	public void test() {
  		System.out.println("实例化后可执行方法");
  	}
  
  	public static void main(String[] args) {
  		for (int i = 0; i < 100; i++) {
  			new Thread(() -> {
  				Singleton04 singleton04 = Singleton04.INSTANCE;
  				System.out.println(Singleton04.INSTANCE.hashCode());
  				singleton04.test();
  			}).start();
  		}
  	}
  }
  ```

## 二、策略模式

思想：定义了一组算法，将每个算法都封装起来，并且使它们之间可以互换。

实现思路：某一种流程，比如开发票，A税率是10，B税率是12，C税率是13，整体流程一致，但是其中的具体开发票的行为，有一些差异，为了减少if else，也为了增加扩展性、清晰性，将开发票计算税率定义为一个接口。每个实现类实现自己的策略。总结起来就是将整体流程中某一个会变动的行为，抽象出来，定义接口实现。不同情况使用不同策略。

缺点：策略比较多的话，策略类也比较多，并且流程中需要清晰知道都有哪些策略。

代码：

1. 接口类，对行为进行抽象

   ```
   //将飞行定义为接口
   public interface Fly {
   	public void fly();
   }
   ```

2. 策略类

   ```
   /**
    * 慢慢的飞
    */
   class SlowFly implements Fly {
   	@Override
   	public void fly() {
   		System.out.println("慢点");
   	}
   }
   
   /**
    * 快速的飞
    */
   class FastFly implements Fly {
   	@Override
   	public void fly() {
   		System.out.println("快点");
   	}
   }
   ```

3. 使用

   ```
   class Bird {
   	public static void main(String[] args) {
   		// 我是鸟，我累了，我想慢点飞
   		Bird bird = new Bird();
   		bird.fly(new SlowFly());
   		System.out.println("终于快到达目的地了");
   		// 快到了，加速，快点飞
   		bird.fly(new FastFly());
   		System.out.println("抵达目的地");
   	}
   }
   ```

## 三、工厂模式

### 1.简单工厂（静态工厂）

- 思路：将创建类的过程交给工厂类，通过不同的参数，返回不同的实例化的子类，将创建类的过程与主要方法体进行解耦。

- 优势：简单。

- 缺点：结构单一，只能针对一种产品进行创建，并且违反了开闭原则。比如造车，新增加一个车类型，工厂类就要新增case一个条件，频繁的改动很麻烦。

- 代码：

  ```
  //1.汽车接口
  public abstract class Car {
  	public abstract String getName();
  }
  
  //具体的车型
  class BmX1Car extends Car {
  	@Override
  	public String getName() {
  		return "我是宝马X1";
  	}
  }
  
  class BmX2Car extends Car {
  	@Override
  	public String getName() {
  		return "我是宝马X2";
  	}
  }
  ```

  ```
  //2.根据不同的条件，创建不同型号的车
  class CarFactory {
  	public static Car getCar(String name) {
  		if ("X1".equals(name)) {
  			return new BmX1Car();
  		} else if ("X2".equals(name)) {
  			return new BmX2Car();
  		} else {
  			return null;
  		}
  	}
  }
  ```

  
