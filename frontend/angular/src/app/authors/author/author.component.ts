import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-author',
  templateUrl: './author.component.html',
  styleUrls: ['./author.component.css']
})
export class AuthorComponent implements OnInit {

  link: string;

  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    this.link = this.route.snapshot.paramMap.get('link');
  }

}
