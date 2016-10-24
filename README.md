# SlideMenuZiDingYi
##正确使用方式:<br/>  
  rootGradle:<br/>  
    allprojects {<br/>  
        repositories {<br/>  
            maven { url "https://jitpack.io" }<br/>  
        }<br/>  
    }<br/>  
    dependencies {<br/>  
    compile 'com.github.cai1449074828:SlideMenuZiDingYi:1.0'<br/>  
}<br/>  
新建<br/>  
```Java
    private YongHuJieMianRight right;
    private  YongHuJieMianLeft left;
    private  YongHuJieMianCenter center;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FangSlideMenu fangSlideMenu=new FangSlideMenu(this);
        setContentView(fangSlideMenu);
        right=new YongHuJieMianRight();
        left=new YongHuJieMianLeft();
        center=new YongHuJieMianCenter(this);
        getSupportFragmentManager().beginTransaction().add(fangSlideMenu.frameLayout3.getId(),right).commit();
        getSupportFragmentManager().beginTransaction().add(fangSlideMenu.frameLayout2.getId(),left).commit();
        getSupportFragmentManager().beginTransaction().add(fangSlideMenu.frameLayout1.getId(),center).commit();
    }
   
