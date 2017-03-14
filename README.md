# SlideMenuZiDingYi
##请使用正确的食用方式:<br/>  
```Java
  rootGradle: 
    allprojects {
        repositories {
            maven { url "https://jitpack.io" }
        }
    }
```
```Java
dependencies { 
    compile 'com.github.cai1449074828:SlideMenuZiDingYi:2.0'
}
```
新建3个fragment<br/>  
```Java
//3个fragment
    private YongHuJieMianRight right;
    private  YongHuJieMianLeft left;
    private  YongHuJieMianCenter center;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FangSlideMenu fangSlideMenu=new FangSlideMenu(this);
        setContentView(fangSlideMenu);
        //设置左右页面宽度百分比,当左右页面不想用时可设为0
        fangSlideMenu.setLayoutWidth(0.5,0);
        right=new YongHuJieMianRight();
        left=new YongHuJieMianLeft();
        center=new YongHuJieMianCenter(this);
        //添加3个fragment
        //getSupportFragmentManager().beginTransaction().add(fangSlideMenu.frameLayout3.getId(),right).commit();
        getSupportFragmentManager().beginTransaction().add(fangSlideMenu.frameLayout2.getId(),left).commit();
        getSupportFragmentManager().beginTransaction().add(fangSlideMenu.frameLayout1.getId(),center).commit();
    }
  ``` 
![](https://github.com/cai1449074828/ImageCache/)  
