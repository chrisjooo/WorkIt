from django.db import models
from django.contrib.auth.models import User, Group

# Create your models here.
class Biodata(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    weight = models.PositiveIntegerField(blank=True, null=True)
    name = models.CharField(max_length=100,blank=True, null=True)
    dateofbirth = models.DateField(blank=True, null=True)
    height = models.PositiveIntegerField(blank=True, null=True)

class Exercise(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    date = models.DateField(auto_now_add=True,blank=True, null=True)
    pushup = models.PositiveIntegerField(default=0)
    situp = models.PositiveIntegerField(default=0)
    jogging = models.FloatField(default=0)

