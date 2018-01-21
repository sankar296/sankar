import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { MyDetailsComponent } from './my-details.component';
import { rootRouterConfig } from './../app.routes';
import { HttpModule } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { APP_BASE_HREF} from '@angular/common';
describe('MyDetailsComponent', () => {
  let component: MyDetailsComponent;
  let fixture: ComponentFixture<MyDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MyDetailsComponent ],
      
      imports: [
        BrowserModule,RouterModule.forRoot(rootRouterConfig),
        HttpModule,FormsModule
      ],
      providers:[{provide: APP_BASE_HREF,useValue: '/' }]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  
  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  it('should render title in a h1 tag1', async(() => {
  const fixture  = TestBed.createComponent(MyDetailsComponent);
  const compiled = fixture.debugElement.nativeElement;
  expect(compiled.querySelector('h1').innerText).toContain('My Details Section');
  }));

  it(`should have as title 'My Details Section'`, async(() => {
    const fixture = TestBed.createComponent(MyDetailsComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('My Details Section');
  }));


  });