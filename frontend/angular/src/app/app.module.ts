import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { RecommendationsComponent } from './widgets/recommendations/recommendations.component';
import { LoginComponent } from './login/login.component';
import { MainMenuComponent } from './common/main-menu/main-menu.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { InterceptorModule } from './interceptor.module';
import { NgxLoggerLevel, LoggerModule } from 'ngx-logger';

const routes: Routes = [
  { path: '', loadChildren: './blog/blog.module#BlogModule' },
  { path: 'admin', loadChildren: './admin/admin.module#AdminModule' },
  { path: 'authors', loadChildren: './authors/authors.module#AuthorsModule' },
  { path: 'blog', loadChildren: './blog/blog.module#BlogModule' },
  { path: 'cms', loadChildren: './cms/cms.module#CmsModule' },
  { path: 'institutions', loadChildren: './institutions/institutions.module#InstitutionsModule' },
  { path: 'market', loadChildren: './market/market.module#MarketModule' },
  { path: 'login', component: LoginComponent },
  { path: '**', component: LoginComponent }
];


@NgModule({
  declarations: [
    AppComponent,
    RecommendationsComponent,
    LoginComponent,
    MainMenuComponent
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    CommonModule,
    FormsModule,
    HttpClientModule,
    InterceptorModule,
    LoggerModule.forRoot({ level: NgxLoggerLevel.DEBUG, serverLogLevel: NgxLoggerLevel.ERROR }),
  ],
  exports: [],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
